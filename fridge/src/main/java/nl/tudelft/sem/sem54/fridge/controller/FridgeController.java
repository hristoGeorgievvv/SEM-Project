package nl.tudelft.sem.sem54.fridge.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequest;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequestResult;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequestStatus;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.IncompleteRequestException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.redis.ProductStatusPublisher;
import nl.tudelft.sem.sem54.fridge.service.LoggedInUserService;
import nl.tudelft.sem.sem54.fridge.service.PermissionService;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductTransactionService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fridge")
public class FridgeController {

    private final UserService userService;
    private final LoggedInUserService loggedInUserService;
    private final ProductService productService;
    private final ProductTransactionService productTransactionService;
    private final ProductStatusPublisher productStatusPublisher;
    private final PermissionService permissionService;

    /**
     * Autowire dependencies.
     *
     * @param userService               user service
     * @param productService            product service
     * @param productTransactionService product transaction service
     * @param productStatusPublisher    product status publisher
     */
    public FridgeController(
            UserService userService,
            ProductService productService,
            ProductTransactionService productTransactionService,
            ProductStatusPublisher productStatusPublisher,
            LoggedInUserService loggedInUserService,
            PermissionService permissionService) {
        this.userService = userService;
        this.productService = productService;
        this.productTransactionService = productTransactionService;
        this.productStatusPublisher = productStatusPublisher;
        this.loggedInUserService = loggedInUserService;
        this.permissionService = permissionService;
    }

    /**
     * Attempts to fulfill a takeoutRequest from the fridge.
     *
     * @param takeoutRequest the request to attempt
     */
    @PostMapping("take")
    public TakeoutRequestResult takeProduct(@RequestBody TakeoutRequest takeoutRequest) {
        if (takeoutRequest.getUsername() == null
                || takeoutRequest.getUsername().isEmpty()
                || takeoutRequest.getAmount() <= 0
                || takeoutRequest.getProductId() <= 0) {
            String incompleteRequestMessage = "The request must contain the following fields in the body: "
                    + "username, "
                    + "amount (> 0), "
                    + "productId (> 0), ";
            throw new IncompleteRequestException(incompleteRequestMessage);
        }

        User user = userService.findByUsername(takeoutRequest.getUsername());

        permissionService.canTakeProduct(user, takeoutRequest);

        Product product = productService.findById(takeoutRequest.getProductId());

        int newPortionsLeft = product.getPortionsLeft() - takeoutRequest.getAmount();

        if (newPortionsLeft < 0) {
            // Cannot process request due to insufficient product

            return new TakeoutRequestResult.Builder()
                    .forProduct(product)
                    .withStatus(TakeoutRequestStatus.INSUFFICIENT)
                    .withDelta(0)
                    .responseTo(takeoutRequest)
                    .build();

        } else if (newPortionsLeft == 0) {
            // Can process request but product finishes -> warn main service
            return processFinishes(product, user, takeoutRequest);

        } else {
            return processNormally(product, user, newPortionsLeft, takeoutRequest);

        }

    }

    /**
     * Attempts to fulfill a takeoutRequest from the fridge.
     * This function is made to allow for the use of a parameterized product usage.
     *
     * @param username The username of the user that will consume a product
     * @param productId The id of the product that is consumed
     * @param amount The amount of portions the user will consume
     * @return the TakeoutRequestResult
     */
    public TakeoutRequestResult takeProduct(String username, long productId, int amount) {
        TakeoutRequest takeoutRequest = new TakeoutRequest(username, productId, amount);
        return this.takeProduct(takeoutRequest);
    }

    private TakeoutRequestResult processNormally(Product product, User user,
                                                 int newPortionsLeft, TakeoutRequest takeoutRequest) {
        // Can process request normally
        product.setPortionsLeft(newPortionsLeft);

        ProductTransaction productTransaction = new ProductTransaction.Builder()
                .setTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .setUser(user)
                .setProduct(product)
                .setCredits(product.getCreditValue())
                .setPortions(takeoutRequest.getAmount())
                .setRevert(false)
                .build();

        productTransactionService.save(productTransaction);
        productService.save(product);

        return new TakeoutRequestResult.Builder()
                .forProduct(product)
                .withStatus(TakeoutRequestStatus.SUFFICIENT)
                .withDelta(-takeoutRequest.getAmount())
                .responseTo(takeoutRequest)
                .build();
    }

    private TakeoutRequestResult processFinishes(Product product, User user,
                                                 TakeoutRequest takeoutRequest) {
        product.setPortionsLeft(0);

        ProductTransaction productTransaction = new ProductTransaction.Builder()
                .setTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .setUser(user)
                .setProduct(product)
                .setCredits(product.getCreditValue())
                .setPortions(takeoutRequest.getAmount())
                .setRevert(false)
                .build();

        productTransactionService.save(productTransaction);

        productService.save(product);

        productStatusPublisher.publishDepletion(product);

        productService.delete(product);

        return new TakeoutRequestResult.Builder()
                .forProduct(product)
                .withStatus(TakeoutRequestStatus.FINISHED)
                .withDelta(-takeoutRequest.getAmount())
                .responseTo(takeoutRequest)
                .build();
    }

    /**
     * Attempts to undo the last transaction of a user.
     *
     * @param productId the Id of the product
     */
    @PostMapping("undo")
    public TakeoutRequestResult undoLastTransaction(@RequestBody long productId) {

        Product product = productService.findById(productId);

        User user = loggedInUserService.getUser();

        ProductTransaction productTransaction =
                productTransactionService.getLastValidTransaction(productId, user.getId());

        int portionsUndo = productTransaction.getPortions();

        product.setPortionsLeft(product.getPortionsLeft() + portionsUndo);

        productService.save(product);

        ProductTransaction productTransactionUndo = new ProductTransaction.Builder()
                .setTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .setUser(user)
                .setProduct(product)
                .setCredits(product.getCreditValue())
                .setPortions(portionsUndo * (-1))
                .setRevert(true)
                .setProductTransaction(productTransaction)
                .build();

        productTransaction.setRevert(true);

        productTransactionService.save(productTransaction);
        productTransactionService.save(productTransactionUndo);

        return new TakeoutRequestResult.Builder()
                .forProduct(product)
                .withStatus(TakeoutRequestStatus.SUFFICIENT)
                .withDelta(portionsUndo)
                .setAmount(0)
                .build();

    }
}
