package nl.tudelft.sem.sem54.fridge.service;

import java.util.Collection;
import java.util.List;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.exceptions.CannotEditException;
import nl.tudelft.sem.sem54.fridge.exceptions.IncompleteRequestException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductExistsException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.repository.ProductRepository;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        if (id <= 0) {
            throw new IncompleteRequestException("Please specify a productId > 0!");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product findByProductName(String productName) {
        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));
    }

    @Override
    public boolean checkIfProductExists(String productName) {
        return productRepository.existsByProductName(productName);
    }

    @Override
    public boolean checkIfProductExists(long productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public Product findByProductNameNotChanged(String productName) {
        Product product = productRepository.findByProductName(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));
        if (product.getPortionsLeft() != product.getPortions()) {
            throw new CannotEditException(product.getId());
        }
        return product;
    }
}
