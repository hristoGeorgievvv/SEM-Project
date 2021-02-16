package nl.tudelft.sem.sem54.fridge.service.base;

import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import java.util.List;
import java.util.Optional;

public interface ProductTransactionService {

    ProductTransaction save(ProductTransaction productTransactionUndo);

    ProductTransaction getLastValidTransaction(long productId, long userId);

}
