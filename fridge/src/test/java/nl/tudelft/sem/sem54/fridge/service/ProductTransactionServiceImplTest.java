package nl.tudelft.sem.sem54.fridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import nl.tudelft.sem.sem54.fridge.exceptions.CannotUndoException;
import nl.tudelft.sem.sem54.fridge.repository.ProductTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductTransactionServiceImplTest {
    @InjectMocks
    ProductTransactionServiceImpl productTransactionService;

    @Mock
    ProductTransactionRepository repository;

    @Test
    void testSave() {
        ProductTransaction productTransaction = mock(ProductTransaction.class);

        when(repository.save(productTransaction)).thenReturn(productTransaction);

        ProductTransaction result = productTransactionService.save(productTransaction);

        assertThat(result).isSameAs(productTransaction);
        verifyNoInteractions(productTransaction);
        verify(repository, times(1)).save(productTransaction);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetLastValidTransaction_exists() {
        ProductTransaction productTransaction = mock(ProductTransaction.class);

        when(repository.findLastValidTransaction(1L, 2L))
            .thenReturn(Optional.of(productTransaction));

        ProductTransaction result = productTransactionService.getLastValidTransaction(1L, 2L);

        assertThat(result).isSameAs(productTransaction);
        verifyNoInteractions(productTransaction);
        verify(repository, times(1)).findLastValidTransaction(1L, 2L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetLastValidTransaction_doesNotExist() {
        ProductTransaction productTransaction = mock(ProductTransaction.class);

        when(repository.findLastValidTransaction(1L, 2L))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productTransactionService.getLastValidTransaction(1L, 2L))
            .isInstanceOf(CannotUndoException.class);

        verifyNoInteractions(productTransaction);
        verify(repository, times(1)).findLastValidTransaction(1L, 2L);
        verifyNoMoreInteractions(repository);
    }

}