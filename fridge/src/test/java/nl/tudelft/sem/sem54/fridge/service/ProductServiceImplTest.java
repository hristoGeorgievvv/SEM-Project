package nl.tudelft.sem.sem54.fridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.exceptions.CannotEditException;
import nl.tudelft.sem.sem54.fridge.exceptions.IncompleteRequestException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private final String testProduct1 = "testProduct";
    private final String happiness = "happiness";
    @InjectMocks
    private transient ProductServiceImpl productServiceMock;

    @Mock
    private transient ProductRepository productRepositoryMock;

    Product testProduct;
    Product testProductChanged;
    List<Product> testProductList;

    @Test
    void save() {
        testProduct = Mockito.mock(Product.class);
        when(productRepositoryMock.save(testProduct)).thenReturn(testProduct);

        assertEquals(testProduct, productServiceMock.save(testProduct));
        verify(productRepositoryMock, times(1)).save(any(Product.class));
        verifyNoInteractions(testProduct);
    }

    @Test
    void delete() {
        testProduct = Mockito.mock(Product.class);

        productServiceMock.delete(testProduct);
        verify(productRepositoryMock, times(1)).delete(any(Product.class));
        verifyNoInteractions(testProduct);
    }

    @Test
    void findAll() {
        testProductList = Mockito.mock(List.class);
        when(productRepositoryMock.findAll()).thenReturn(testProductList);

        assertEquals(testProductList, productServiceMock.findAll());
        verify(productRepositoryMock, times(1)).findAll();
        verifyNoInteractions(testProductList);
    }

    @Test
    void findById_lessZero() {
        assertThrows(IncompleteRequestException.class,
                () -> productServiceMock.findById(0L));

        assertThrows(IncompleteRequestException.class,
                () -> productServiceMock.findById(-1L));
    }

    @Test
    void findById_ok() {
        testProduct = Mockito.mock(Product.class);
        when(productRepositoryMock.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(testProduct));

        assertEquals(testProduct, productServiceMock.findById(1L));
        verify(productRepositoryMock, times(1)).findById(1L);
        verifyNoInteractions(testProduct);
    }

    @Test
    void findById_nOk() {
        testProduct = Mockito.mock(Product.class);
        when(productRepositoryMock.findById(2L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productServiceMock.findById(2L));

        verifyNoInteractions(testProduct);
    }

    @Test
    void findByProductName_ok() {
        testProduct = Mockito.mock(Product.class);
        when(productRepositoryMock.findByProductName(testProduct1))
                .thenReturn(java.util.Optional.ofNullable(testProduct));

        assertEquals(testProduct, productServiceMock.findByProductName(testProduct1));
        verify(productRepositoryMock, times(1))
                .findByProductName(testProduct1);
        verifyNoInteractions(testProduct);
    }

    @Test
    void findByProductName_nOk() {
        testProduct = Mockito.mock(Product.class);
        when(productRepositoryMock.findByProductName(happiness))
                .thenReturn(java.util.Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productServiceMock.findByProductName(happiness));

        verifyNoInteractions(testProduct);
    }

    @Test
    void checkIfProductExists_ok() {
        when(productRepositoryMock.existsByProductName(testProduct1))
                .thenReturn(true);

        assertTrue(productServiceMock.checkIfProductExists(testProduct1));
        verify(productRepositoryMock, times(1))
                .existsByProductName(testProduct1);
    }

    @Test
    void checkIfProductExists_nOk() {
        when(productRepositoryMock.existsByProductName(happiness))
                .thenReturn(false);

        assertFalse(productServiceMock.checkIfProductExists(happiness));
        verify(productRepositoryMock, times(1))
                .existsByProductName(happiness);
    }

    @Test
    void testCheckIfProductExists_ok() {
        when(productRepositoryMock.existsById(1L)).thenReturn(true);

        assertTrue(productServiceMock.checkIfProductExists(1L));
        verify(productRepositoryMock, times(1)).existsById(1L);
    }

    @Test
    void testCheckIfProductExists_nOk() {
        when(productRepositoryMock.existsById(2L)).thenReturn(false);

        assertFalse(productServiceMock.checkIfProductExists(2L));
        verify(productRepositoryMock, times(1)).existsById(2L);
    }

    @Test
    void findByProductNameNotChanged_empty() {
        when(productRepositoryMock.findByProductName(happiness))
                .thenReturn(java.util.Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productServiceMock.findByProductNameNotChanged(happiness));

        verify(productRepositoryMock, times(1))
                .findByProductName(happiness);
    }

    @Test
    void findByProductNameNotChanged_ok() {
        testProduct = Mockito.mock(Product.class);

        when(productRepositoryMock.findByProductName(testProduct1))
                .thenReturn(java.util.Optional.ofNullable(testProduct));
        when(testProduct.getPortionsLeft()).thenReturn(5);
        when(testProduct.getPortions()).thenReturn(5);

        assertEquals(testProduct, productServiceMock
                .findByProductNameNotChanged(testProduct1));
        verify(productRepositoryMock, times(1))
                .findByProductName(testProduct1);
        verify(testProduct, times(1)).getPortionsLeft();
        verify(testProduct, times(1)).getPortions();
    }

    @Test
    void findByProductNameNotChanged_nOk() {
        testProductChanged = Mockito.mock(Product.class);

        when(productRepositoryMock.findByProductName("badProduct"))
                .thenReturn(java.util.Optional.ofNullable(testProductChanged));
        when(testProductChanged.getPortionsLeft()).thenReturn(2);
        when(testProductChanged.getPortions()).thenReturn(5);

        assertThrows(CannotEditException.class,
                () -> productServiceMock.findByProductNameNotChanged("badProduct"));
        verify(productRepositoryMock, times(1))
                .findByProductName("badProduct");
        verify(testProductChanged, times(1))
                .getPortionsLeft();
        verify(testProductChanged, times(1))
                .getPortions();
    }
}