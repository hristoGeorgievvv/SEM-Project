package nl.tudelft.sem.sem54.fridge.controller;

import java.util.List;
import nl.tudelft.sem.sem54.fridge.controller.pojo.ProductEntryRequest;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.CannotEditException;
import nl.tudelft.sem.sem54.fridge.exceptions.IncompleteRequestException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductExistsException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.redis.ProductStatusPublisher;
import nl.tudelft.sem.sem54.fridge.service.LoggedInUserService;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("fridge")
public class ProductController {

    private final String productsEndpoint = "products";
    private final LoggedInUserService loggedInUserService;
    private final FridgeService fridgeService;
    private final ProductService productService;
    private final ProductStatusPublisher productStatusPublisher;

    /**
     * Autowire dependencies.
     *
     * @param fridgeService             fridge service
     * @param productService            product service
     * @param productStatusPublisher    product status publisher
     * @param loggedInUserService       loggedinservice
     */
    public ProductController(
            FridgeService fridgeService,
            ProductService productService,
            ProductStatusPublisher productStatusPublisher,
            LoggedInUserService loggedInUserService) {
        this.fridgeService = fridgeService;
        this.productService = productService;
        this.productStatusPublisher = productStatusPublisher;
        this.loggedInUserService = loggedInUserService;
    }


    /**
     * Attempts to fulfill a product removal request by a user.
     * User and removed product must be in the fridge.
     * User must be the owner of the product.
     *
     * @param productId the id of the product.
     */
    @DeleteMapping(productsEndpoint)
    public void removeProduct(@RequestBody long productId) {
        if (productId <= 0) {
            throw new IncompleteRequestException("Please specify a productId > 0!");
        }

        if (!productService.checkIfProductExists(productId)) {
            throw new ProductNotFoundException(productId);
        }

        Product product = productService.findById(productId);
        User user = loggedInUserService.getUser();

        if (user != product.getOwner()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        productStatusPublisher.publishRemoval(product);

        productService.delete(product);
    }

    /**
     * Attempts to fulfill a product edit request by a user.
     * The product to edit must be in the fridge.
     * The user must be the owner of the product.
     * The product must be in full (no portions taken).
     *
     * @param productEntryRequest a request containing the edited product.
     */
    @PatchMapping(productsEndpoint)
    public Product editProduct(@RequestBody ProductEntryRequest productEntryRequest) {

        ProductEntryRequest.checkValidFields(productEntryRequest);

        User user = loggedInUserService.getUser();
        String productName = productEntryRequest.getProductName();

        Product product = productService.findByProductNameNotChanged(productName);

        if (user != product.getOwner()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // once we are sure that the requester owns the product
        // and nobody has taken a portion, we can fulfill the request
        Product editedProduct = new Product.Builder()
                .setProductName(productName)
                .setOwner(user)
                .setPortions(productEntryRequest.getPortions())
                .setCreditValue(productEntryRequest.getCreditValue())
                .setExpirationDate(productEntryRequest.getExpirationDate())
                .setFridge(fridgeService.getDefaultFridge())
                .build();

        editedProduct.setId(product.getId());

        return productService.save(editedProduct);
    }


    /**
     * Returns a list of all products.
     */
    @GetMapping(productsEndpoint)
    public List<Product> getProducts() {
        Fridge fridge = fridgeService.getDefaultFridge();
        return fridge.getAvailableProducts();
    }



    /**
     * Attempts to add a product.
     *
     * @param productEntryRequest the request to attempt
     */

    @PostMapping(productsEndpoint)
    public Product addProduct(@RequestBody ProductEntryRequest productEntryRequest) {
        User user = loggedInUserService.getUser();

        if (productService.checkIfProductExists(productEntryRequest.getProductName())) {
            throw new ProductExistsException(productEntryRequest.getProductName());
        }

        if (productEntryRequest.getProductName() == null
                || productEntryRequest.getProductName().isEmpty()
                || productEntryRequest.getCreditValue() <= 0
                || productEntryRequest.getPortions() <= 0
                || productEntryRequest.getExpirationDate() == null) {
            throw new IncompleteRequestException(
                    "The request must contain the following fields in the body: "
                            + "productName, "
                            + "portions (> 0), "
                            + "creditValue (> 0), "
                            + "expirationDate (YYYY-MM-DD)!");

        }

        Fridge fridge = fridgeService.getDefaultFridge();

        Product product = new Product.Builder()
                .setProductName(productEntryRequest.getProductName())
                .setOwner(user)
                .setPortions(productEntryRequest.getPortions())
                .setPortionsLeft(productEntryRequest.getPortions())
                .setCreditValue(productEntryRequest.getCreditValue())
                .setExpirationDate(productEntryRequest.getExpirationDate())
                .setFridge(fridge)
                .build();

        return productService.save(product);

    }

}
