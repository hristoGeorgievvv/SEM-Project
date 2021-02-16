package nl.tudelft.sem.sem54.fridge.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import nl.tudelft.sem.sem54.fridge.controller.pojo.ProductEntryRequest;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequest;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequestResult;
import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequestStatus;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class FridgeControllerTest {
    @InjectMocks
    private FridgeController fridgeController;

    @Spy
    private ProductService productService;

    @Spy
    private ProductTransactionService productTransactionService;

    @Mock
    private FridgeService fridgeService;

    @Mock
    private LoggedInUserService loggedInUserService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private UserService userService;

    @Mock
    private ProductStatusPublisher productStatusPublisher;

    private String username;
    private String productName;
    private Fridge fridge;
    private User user;
    private Product product;
    private ProductEntryRequest productEntryRequest;
    private TakeoutRequest takeoutRequest;

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
        takeoutRequest = new TakeoutRequest(username, 1L, 10);
    }

    @Test
    public void contextLoads() {
        assertThat(fridgeController).isNotNull();
    }


    @Test
    public void testProductRequest_verifyUserServiceCall() {
        when(productService.findById(1L)).thenReturn(product);
        when(userService.findByUsername(username)).thenReturn(user);

        fridgeController.takeProduct(takeoutRequest);
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    public void testProductRequest_nullUsername() {
        takeoutRequest.setUsername(null);
        assertThrows(IncompleteRequestException.class,
                () -> fridgeController.takeProduct(takeoutRequest));
    }

    @Test
    public void testProductRequest_emptyUsername() {
        takeoutRequest.setUsername("");
        assertThrows(IncompleteRequestException.class,
                () -> fridgeController.takeProduct(takeoutRequest));
    }

    @Test
    public void testProductRequest_zeroAmount() {
        takeoutRequest.setAmount(0);
        assertThrows(IncompleteRequestException.class,
                () -> fridgeController.takeProduct(takeoutRequest));
    }

    @Test
    public void testProductRequest_zeroProductId() {
        takeoutRequest.setProductId(0);
        assertThrows(IncompleteRequestException.class,
                () -> fridgeController.takeProduct(takeoutRequest));
    }

    @Test
    public void testProductRequest_productDoesNotExist() {
        when(userService.findByUsername(username)).thenReturn(user);
        assertThrows(NullPointerException.class,
                () -> fridgeController.takeProduct(takeoutRequest));
    }

    @Test
    public void testProductRequest_differentFridge() {
        Fridge fridge1 = new Fridge();
        fridge1.setProducts(Collections.singletonList(new Product()));

        when(userService.findByUsername(username)).thenReturn(user);

        doThrow(ResponseStatusException.class)
                .when(permissionService).canTakeProduct(any(User.class), any(TakeoutRequest.class));

        assertThrows(ResponseStatusException.class,
                () -> fridgeController.takeProduct(takeoutRequest));
    }


    @Test
    public void testProductRequest_isSufficient() {
        when(userService.findByUsername(username)).thenReturn(user);
        when(productService.findById(1L)).thenReturn(product);
        when(productTransactionService.save(any())).thenReturn(null);
        doNothing().when(permissionService).canTakeProduct(any(User.class), any(TakeoutRequest.class));
        takeoutRequest.setAmount(9);

        TakeoutRequestResult responseEntity = fridgeController.takeProduct(takeoutRequest);

        assertThat(responseEntity.getProductStatus()).isEqualTo(TakeoutRequestStatus.SUFFICIENT);
        assertThat(responseEntity.getPortionsLeft()).isEqualTo(1);
        assertThat(responseEntity.getDelta()).isEqualTo(-takeoutRequest.getAmount());
    }

    @Test
    public void test_take_product() {
        String username = "100";
        long productId = 100;
        int amount = 9;

        when(userService.findByUsername(username)).thenReturn(user);
        when(productService.findById(productId)).thenReturn(product);
        when(productTransactionService.save(any())).thenReturn(null);
        doNothing().when(permissionService).canTakeProduct(any(User.class), any(TakeoutRequest.class));

        TakeoutRequestResult responseEntity = fridgeController.takeProduct(username, productId, amount);

        assertThat(responseEntity.getProductStatus()).isEqualTo(TakeoutRequestStatus.SUFFICIENT);
        assertThat(responseEntity.getPortionsLeft()).isEqualTo(1);
        assertThat(responseEntity.getDelta()).isEqualTo(amount * (-1));

    }


    @Test
    public void testProductRequest_isInsufficient() {
        when(userService.findByUsername(takeoutRequest.getUsername())).thenReturn(user);
        when(productService.findById(product.getId())).thenReturn(product);
        takeoutRequest.setAmount(11);
        TakeoutRequestResult responseEntity = fridgeController.takeProduct(takeoutRequest);

        assertThat(responseEntity.getProductStatus())
                .isEqualTo(TakeoutRequestStatus.INSUFFICIENT);
        assertThat(responseEntity.getPortionsLeft())
                .isEqualTo(product.getPortionsLeft());
    }

    @Test
    public void testProductRequest_isFinished() {
        when(userService.findByUsername(takeoutRequest.getUsername())).thenReturn(user);
        when(productService.findById(any())).thenReturn(product);

        product.setOwner(user);

        takeoutRequest.setAmount(10);
        TakeoutRequestResult responseEntity = fridgeController.takeProduct(takeoutRequest);
        verify(productStatusPublisher, times(1)).publishDepletion(product);

        assertThat(responseEntity.getProductStatus()).isEqualTo(TakeoutRequestStatus.FINISHED);
        assertThat(responseEntity.getPortionsLeft()).isEqualTo(0);

        assertThat(responseEntity.getDelta()).isEqualTo(-takeoutRequest.getAmount());
        verify(productService).delete(eq(product));
    }



    @Test
    public void testUndoLastTransaction_success() {
        ProductTransaction productTransaction = new ProductTransaction(
                Timestamp.valueOf(LocalDateTime.now()), user, product,
                product.getCreditValue(),
                5, false, null);

        TakeoutRequest takeoutRequest = new TakeoutRequest(product.getId(), 1);

        when(loggedInUserService.getUser()).thenReturn(user);

        when(productTransactionService.getLastValidTransaction(takeoutRequest.getProductId(), user.getId()))
                .thenReturn(productTransaction);

        when(productService.findById(product.getId())).thenReturn(product);


        TakeoutRequestResult takeoutRequestResult = fridgeController.undoLastTransaction(product.getId());

        assertEquals(productTransaction.getPortions(), takeoutRequestResult.getDelta());

        Product productSaved = new Product(productName,
                user,
                10,
                product.getCreditValue(),
               product.getExpirationDate(), fridge);

        productSaved.setPortionsLeft(15);

        verify(productService).save(eq(productSaved));


        ProductTransaction productTransactionReverted = new ProductTransaction(
                Timestamp.valueOf(LocalDateTime.now()), user, product,
                product.getCreditValue(),
                5, true, null);

        ProductTransaction productTransactionUndo = new ProductTransaction(
                Timestamp.valueOf(LocalDateTime.now()), user, product,
                product.getCreditValue(),
                -5, true, productTransaction);

        verify(productTransactionService).save(eq(productTransactionReverted));
        verify(productTransactionService).save(eq(productTransactionUndo));


    }
}
