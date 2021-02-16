package nl.tudelft.sem.sem54.fridge.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import nl.tudelft.sem.sem54.fridge.controller.pojo.MealRequest;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.NotEnoughPortionsException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.exceptions.UserNotFoundException;
import nl.tudelft.sem.sem54.fridge.repository.UserRepository;
import nl.tudelft.sem.sem54.fridge.service.ProductServiceImpl;
import nl.tudelft.sem.sem54.fridge.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MealControllerTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private FridgeController fridgeController;

    @Mock
    private ProductServiceImpl productServiceImpl;

    private MealController mealController;
    private User user1;
    private User user2;
    private User user3;
    private Product p1;
    private Product p2;
    private Product p3;
    private Product p4;

    @BeforeEach
    void beforeEach() {

        Fridge fridge = new Fridge();

        user1 = new User("test0", fridge);
        user2 = new User("test1", fridge);
        user3 = new User("test2", fridge);

        // Create products
        p1 = new Product("cheese", user1, 10, 1,
                Timestamp.valueOf(LocalDateTime.now()), fridge);
        p2 = new Product("milk", user1, 10, 1,
                Timestamp.valueOf(LocalDateTime.now()), fridge);
        p3 = new Product("nuts", user1, 10, 1,
                Timestamp.valueOf(LocalDateTime.now()), fridge);
        p4 = new Product("chocolate", user1, 11, 1,
                Timestamp.valueOf(LocalDateTime.now()), fridge);

        p1.setId(0L);
        p2.setId(1L);
        p3.setId(2L);
        p4.setId(3L);

        lenient().when(userRepositoryMock.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        lenient().when(userRepositoryMock.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        lenient().when(userRepositoryMock.findByUsername(user3.getUsername())).thenReturn(Optional.of(user3));
        lenient().when(userRepositoryMock.findByUsername("userDoesNotExist")).thenReturn(Optional.empty());

        lenient().when(productServiceImpl.findById(p1.getId())).thenReturn(p1);
        lenient().when(productServiceImpl.findById(p2.getId())).thenReturn(p2);
        lenient().when(productServiceImpl.findById(p3.getId())).thenReturn(p3);
        lenient().when(productServiceImpl.findById(p4.getId())).thenReturn(p4);
        lenient().when(productServiceImpl.findById(4L)).thenThrow(new ProductNotFoundException(4L));

        mealController = new MealController(userServiceImpl, productServiceImpl, fridgeController);
    }

    @Test
    @WithMockUser
    void testProcessMeal_success() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), user3.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 3);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 1);

        MealRequest mr = new MealRequest(usernames, products);

        mealController.processMeal(mr);
        // Verify that we take 9 products (three products per user)
        verify(fridgeController, times(9)).takeProduct(anyString(), anyLong(), anyInt());
    }

    @Test
    @WithMockUser
    void testProcessMeal_successPortionBoundary() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 5);
        products.put(p2.getId(), 5);
        products.put(p3.getId(), 5);

        MealRequest mr = new MealRequest(usernames, products);

        mealController.processMeal(mr);
        // Verify that we take 9 products (three products per user)
        verify(fridgeController, times(6)).takeProduct(anyString(), anyLong(), anyInt());
    }

    @Test
    @WithMockUser
    void testProcessMeal_tooFewPortionBoundary() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), user3.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();
        // This should fail as P4 has 11 portions and we are asking 12.
        products.put(p4.getId(), 4);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 3);

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(NotEnoughPortionsException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_tooFewUsers() {
        // Create data to test
        List<String> usernames = Collections.singletonList(user1.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 3);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 1);

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(ResponseStatusException.class, () -> mealController.processMeal(mr));
        // Verify that we take nothing
        verify(fridgeController, times(0)).takeProduct(anyString(), anyLong(), anyInt());
    }

    @Test
    @WithMockUser
    void testProcessMeal_tooFewPortions() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), user3.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();
        // The reason this request is NOK is that we need 15 portions of cheese while we only have 10
        products.put(p1.getId(), 5);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 1);

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(NotEnoughPortionsException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_userDoesNotExist() {
        // Create data to test
        // This should fail as one of the users does not exist
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), "userDoesNotExist");
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 3);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 1);

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(UserNotFoundException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_productDoesNotExist() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), user3.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 3);
        products.put(p2.getId(), 3);
        // The product with ID 4L does not exist
        products.put(4L, 1);

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(ProductNotFoundException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_nullUsers() {
        // Create data to test
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 3);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 1);

        MealRequest mr = new MealRequest(null, products);

        assertThrows(ResponseStatusException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_zeroUsers() {
        // Create data to test
        List<String> usernames = new ArrayList<>();
        HashMap<Long, Integer> products = new HashMap<>();
        products.put(p1.getId(), 3);
        products.put(p2.getId(), 3);
        products.put(p3.getId(), 1);

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(ResponseStatusException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_nullProducts() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), user3.getUsername());

        MealRequest mr = new MealRequest(usernames, null);

        assertThrows(ResponseStatusException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }

    @Test
    @WithMockUser
    void testProcessMeal_zeroProducts() {
        // Create data to test
        List<String> usernames = Arrays.asList(user1.getUsername(),
                user2.getUsername(), user3.getUsername());
        HashMap<Long, Integer> products = new HashMap<>();

        MealRequest mr = new MealRequest(usernames, products);

        assertThrows(ResponseStatusException.class, () -> mealController.processMeal(mr));
        // Verify that no products are altered
        verify(fridgeController, times(0)).takeProduct(any());
    }
}
