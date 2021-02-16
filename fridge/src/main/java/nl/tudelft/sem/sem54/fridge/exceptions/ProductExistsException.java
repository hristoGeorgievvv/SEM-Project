package nl.tudelft.sem.sem54.fridge.exceptions;

public class ProductExistsException extends RuntimeException {
    public static final long serialVersionUID = 432050;

    public ProductExistsException(String productName) {
        super(String.format("Product with productName %s already exists", productName));
    }
}
