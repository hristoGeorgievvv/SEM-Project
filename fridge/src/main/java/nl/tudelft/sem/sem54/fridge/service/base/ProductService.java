package nl.tudelft.sem.sem54.fridge.service.base;

import nl.tudelft.sem.sem54.fridge.domain.Product;
import java.util.Collection;
import java.util.List;

public interface ProductService {
    Product save(Product product);

    void delete(Product product);

    List<Product> findAll();

    Product findById(Long id);

    Product findByProductName(String productName);

    boolean checkIfProductExists(String productName);

    boolean checkIfProductExists(long productId);
    
    Product findByProductNameNotChanged(String productName);
}

