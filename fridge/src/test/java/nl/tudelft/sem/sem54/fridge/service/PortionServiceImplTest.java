package nl.tudelft.sem.sem54.fridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import nl.tudelft.sem.sem54.fridge.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PortionServiceImplTest {

    @InjectMocks
    PortionServiceImpl portionService;

    @Test
    void testGetUserPortionMap_normalUsage() {
        ProductTransaction pt1 = new ProductTransaction
            .Builder()
            .setUser(new User("user1"))
            .setPortions(1)
            .build();

        ProductTransaction pt2 = new ProductTransaction
            .Builder()
            .setUser(new User("user2"))
            .setPortions(2)
            .build();

        ProductTransaction pt3 = new ProductTransaction
            .Builder()
            .setUser(new User("user3"))
            .setPortions(3)
            .build();

        Collection<ProductTransaction> list = new ArrayList<>();
        list.add(pt1);
        list.add(pt2);
        list.add(pt3);

        Product product = mock(Product.class);

        when(product.getTransactionList()).thenReturn(list);

        Map<String, Integer> userPortionMap = portionService.getUserPortionMap(product);

        verify(product, times(1)).getTransactionList();
        verifyNoMoreInteractions(product);

        assertThat(userPortionMap)
            .containsExactly(entry("user1", 1), entry("user2", 2), entry("user3", 3));
    }

    @Test
    void testGetUserPortionMap_duplicateUser() {
        ProductTransaction pt1 = new ProductTransaction
            .Builder()
            .setUser(new User("user4"))
            .setPortions(1)
            .build();

        ProductTransaction pt2 = new ProductTransaction
            .Builder()
            .setUser(new User("user5"))
            .setPortions(2)
            .build();

        ProductTransaction pt3 = new ProductTransaction
            .Builder()
            .setUser(new User("user5"))
            .setPortions(3)
            .build();

        Collection<ProductTransaction> list = new ArrayList<>();
        list.add(pt1);
        list.add(pt2);
        list.add(pt3);

        Product product = mock(Product.class);

        when(product.getTransactionList()).thenReturn(list);

        Map<String, Integer> userPortionMap = portionService.getUserPortionMap(product);

        verify(product, times(1)).getTransactionList();
        verifyNoMoreInteractions(product);

        assertThat(userPortionMap)
            .containsExactly(entry("user5", 5), entry("user4", 1));
    }

    @Test
    void testGetUserPortionMap_oneUser() {
        ProductTransaction pt1 = new ProductTransaction
            .Builder()
            .setUser(new User("user6"))
            .setPortions(1)
            .build();

        Collection<ProductTransaction> list = new ArrayList<>();
        list.add(pt1);

        Product product = mock(Product.class);

        when(product.getTransactionList()).thenReturn(list);

        Map<String, Integer> userPortionMap = portionService.getUserPortionMap(product);

        verify(product, times(1)).getTransactionList();
        verifyNoMoreInteractions(product);

        assertThat(userPortionMap)
            .containsExactly(entry("user6", 1));
    }

    @Test
    void testGetUserPortionMap_zeroUsers() {
        Collection<ProductTransaction> list = new ArrayList<>();

        Product product = mock(Product.class);

        when(product.getTransactionList()).thenReturn(list);

        Map<String, Integer> userPortionMap = portionService.getUserPortionMap(product);

        verify(product, times(1)).getTransactionList();
        verifyNoMoreInteractions(product);

        assertThat(userPortionMap).isEmpty();
    }
}