package nl.tudelft.sem.sem54.fridge.exceptions;

import java.util.List;

public class ProductNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 432051;

    public ProductNotFoundException(Long id) {
        super(String.format("Product with Id %d not found", id));
    }

    public ProductNotFoundException(String productName) {
        super(String.format("Product with productName %s not found", productName));
    }

    public ProductNotFoundException(List<Long> productIds) {
        super("Products with IDs " + productIds.toString() + " not found");
    }
}
