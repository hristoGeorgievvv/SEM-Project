package nl.tudelft.sem.sem54.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

import nl.tudelft.sem.sem54.fridge.controller.pojo.ProductEntryRequest;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequest;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequestResult;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequestStatus;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.CannotEditException;
import nl.tudelft.sem.sem54.fridge.exceptions.IncompleteRequestException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductExistsException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.redis.ProductStatusPublisher;
import nl.tudelft.sem.sem54.fridge.service.LoggedInUserService;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductTransactionService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;


    @Mock
    private FridgeService fridgeService;

    @Mock
    private LoggedInUserService loggedInUserService;


    @Mock
    private ProductStatusPublisher productStatusPublisher;

    private String username;
    private String productName;
    private Fridge fridge;
    private User user;
    private Product product;
    private ProductEntryRequest productEntryRequest;

    /**
     * Setup variables for the testing environment.
     */
    @BeforeEach
    public void beforeEach() {
        username = UUID.randomUUID().toString();
        productName = UUID.randomUUID().toString();

        user = new User(username);
        fridge = new Fridge();
        user.setFridge(fridge);

        product = new Product(productName,
                user, 10, 10,
                new Date(System.currentTimeMillis() + 1000000), fridge);
        product.setId(1L);
        productEntryRequest = new ProductEntryRequest(product);
    }

    @Test
    public void contextLoads() {
        assertThat(productController).isNotNull();
    }

    @Test
    public void testAddProduct_nullProductName() {
        productEntryRequest.setProductName(null);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.addProduct(productEntryRequest));
    }

    @Test
    public void testAddProduct_emptyProductName() {
        productEntryRequest.setProductName("");
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.addProduct(productEntryRequest));
    }

    @Test
    public void testAddProduct_zeroCreditValue() {
        productEntryRequest.setCreditValue(0);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.addProduct(productEntryRequest));
    }

    @Test
    public void testAddProduct_zeroPortions() {
        productEntryRequest.setPortions(0);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.addProduct(productEntryRequest));
    }

    @Test
    public void testAddProduct_nullExpirationDate() {
        productEntryRequest.setExpirationDate(null);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.addProduct(productEntryRequest));
    }

    @Test
    public void testAddProduct_alreadyExists() {
        when(productService.checkIfProductExists(product.getProductName())).thenReturn(true);

        ProductEntryRequest productEntryRequest = new ProductEntryRequest(product);

        Assertions.assertThrows(ProductExistsException.class, () ->
                productController.addProduct(productEntryRequest));
    }

    @Test
    public void testAddProduct_success() {
        when(productService.checkIfProductExists(product.getProductName())).thenReturn(false);
        when(loggedInUserService.getUser()).thenReturn(user);
        when(fridgeService.getDefaultFridge()).thenReturn(fridge);

        ProductEntryRequest productEntryRequest = new ProductEntryRequest(product);

        productController.addProduct(productEntryRequest);

        verify(productService, times(1)).save(eq(product));
    }

    @Test
    public void testEditProduct_nullProductName() {
        productEntryRequest.setProductName(null);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.editProduct(productEntryRequest));
    }

    @Test
    public void testEditProduct_emptyProductName() {
        productEntryRequest.setProductName("");
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.editProduct(productEntryRequest));
    }

    @Test
    public void testEditProduct_zeroCreditValue() {
        productEntryRequest.setCreditValue(0);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.editProduct(productEntryRequest));
    }

    @Test
    public void testEditProduct_zeroPortions() {
        productEntryRequest.setPortions(0);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.editProduct(productEntryRequest));
    }

    @Test
    public void testEditProduct_nullExpirationDate() {
        productEntryRequest.setExpirationDate(null);
        Assertions.assertThrows(IncompleteRequestException.class,
                () -> productController.editProduct(productEntryRequest));
    }


    @Test
    public void testEditProduct_userIsNotOwner() {
        when(loggedInUserService.getUser()).thenReturn(user);

        when(productService.findByProductNameNotChanged(productName)).thenReturn(product);

        product.setOwner(new User(username + "x"));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> productController.editProduct(productEntryRequest));
    }


    @Test
    public void testEditProduct_success() {
        when(loggedInUserService.getUser()).thenReturn(user);

        when(productService.findByProductNameNotChanged(productName)).thenReturn(product);
        when(fridgeService.getDefaultFridge()).thenReturn(product.getFridge());
        when(productService.save(any())).thenReturn(product);

        assertEquals(productController.editProduct(productEntryRequest), product);
    }





    @Test
    public void testRemoveProduct_zeroProductId() {
        assertThrows(IncompleteRequestException.class,
                () -> productController.removeProduct(0));
    }

    @Test
    public void testRemoveProduct_productDoesNotExist() {
        when(productService.checkIfProductExists(product.getId())).thenReturn(false);
        assertThrows(ProductNotFoundException.class,
                () -> productController.removeProduct(product.getId()));

    }

    @Test
    public void testRemoveProduct_notOwner() {
        when(productService.checkIfProductExists(product.getId())).thenReturn(true);
        when(productService.findById(product.getId())).thenReturn(product);

        product.setOwner(new User("vanko1"));

        assertThrows(
                ResponseStatusException.class,
                () -> productController.removeProduct(product.getId()));

    }

    @Test
    public void testRemoveProduct_success() {
        when(productService.checkIfProductExists(product.getId())).thenReturn(true);
        when(productService.findById(product.getId())).thenReturn(product);
        when(loggedInUserService.getUser()).thenReturn(user);
        doNothing().when(productService).delete(product);

        assertDoesNotThrow(() -> productController.removeProduct(product.getId()));
    }

}
