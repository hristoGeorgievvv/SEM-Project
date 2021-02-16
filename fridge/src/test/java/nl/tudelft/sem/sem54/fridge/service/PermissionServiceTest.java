package nl.tudelft.sem.sem54.fridge.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.sem54.fridge.controller.pojo.TakeoutRequest;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Mock
    private FridgeServiceImpl fridgeServiceMock;

    @Mock
    private ProductServiceImpl productService;

    private User user;
    private TakeoutRequest takeoutRequest;

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID().toString(), mock(Fridge.class));
        takeoutRequest = new TakeoutRequest(UUID.randomUUID().toString(), 10, 5);
    }

    @Test
    public void user_has_no_permission_to_fridge_test() {
        Fridge fridge = mock(Fridge.class);
        when(fridgeServiceMock.getDefaultFridge()).thenReturn(fridge);

        assertThrows(ResponseStatusException.class, () -> permissionService.canTakeProduct(user, takeoutRequest));

    }


    @Test
    public void product_does_not_exist_test() {
        when(fridgeServiceMock.getDefaultFridge()).thenReturn(user.getFridge());
        when(productService.checkIfProductExists(eq(takeoutRequest.getProductId()))).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> permissionService.canTakeProduct(user, takeoutRequest));
    }

    @Test
    public void amount_is_smaller_than_0_test() {
        when(fridgeServiceMock.getDefaultFridge()).thenReturn(user.getFridge());
        when(productService.checkIfProductExists(eq(takeoutRequest.getProductId()))).thenReturn(true);
        takeoutRequest.setAmount(-5);
        assertThrows(ResponseStatusException.class, () -> permissionService.canTakeProduct(user, takeoutRequest));

    }


}
