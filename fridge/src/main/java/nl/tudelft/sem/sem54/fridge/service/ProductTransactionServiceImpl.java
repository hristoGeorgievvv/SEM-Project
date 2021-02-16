package nl.tudelft.sem.sem54.fridge.service;

import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import nl.tudelft.sem.sem54.fridge.exceptions.CannotUndoException;
import nl.tudelft.sem.sem54.fridge.repository.ProductTransactionRepository;
import nl.tudelft.sem.sem54.fridge.service.base.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTransactionServiceImpl implements ProductTransactionService {

    private ProductTransactionRepository productTransactionRepository;

    ProductTransactionServiceImpl(ProductTransactionRepository productTransactionRepository) {
        this.productTransactionRepository = productTransactionRepository;
    }

    @Override
    public ProductTransaction save(ProductTransaction productTransactionUndo) {
        return productTransactionRepository.save(productTransactionUndo);
    }

    @Override
    public ProductTransaction getLastValidTransaction(long productId, long userId) {
        return productTransactionRepository
                .findLastValidTransaction(productId, userId)
                .orElseThrow(() -> new CannotUndoException(productId, userId));
    }

}
