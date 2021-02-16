package nl.tudelft.sem.sem54.fridge.service;

import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequest;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.redis.ProductStatusPublisher;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductTransactionService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PermissionService {
    private final UserService userService;
    private final LoggedInUserService loggedInUserService;
    private final FridgeService fridgeService;
    private final ProductService productService;
    private final ProductTransactionService productTransactionService;
    private final ProductStatusPublisher productStatusPublisher;

    /**
     * Autowire dependencies.
     *
     * @param userService               user service
     * @param fridgeService             fridge service
     * @param productService            product service
     * @param productTransactionService product transaction service
     * @param productStatusPublisher    product status publisher
     */
    public PermissionService(
            UserService userService,
            FridgeService fridgeService,
            ProductService productService,
            ProductTransactionService productTransactionService,
            ProductStatusPublisher productStatusPublisher, LoggedInUserService loggedInUserService) {
        this.userService = userService;
        this.fridgeService = fridgeService;
        this.productService = productService;
        this.productTransactionService = productTransactionService;
        this.productStatusPublisher = productStatusPublisher;
        this.loggedInUserService = loggedInUserService;
    }

    /**
     * Throws an exception if the permission is not granted.
     *
     * @param user user in the request
     * @param takeoutRequest the request
     */
    public void canTakeProduct(User user, TakeoutRequest takeoutRequest) {
        // User has no permission to access fridge
        if (!user.getFridge().equals(fridgeService.getDefaultFridge())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (!productService.checkIfProductExists(takeoutRequest.getProductId())) {
            throw new ProductNotFoundException(takeoutRequest.getProductId());
        }

        if (takeoutRequest.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
