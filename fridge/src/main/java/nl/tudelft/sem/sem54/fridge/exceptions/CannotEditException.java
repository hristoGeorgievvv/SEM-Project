package nl.tudelft.sem.sem54.fridge.exceptions;

public class CannotEditException extends RuntimeException {
    public static final long serialVersionUID = 832049;

    public CannotEditException(String s) {
        super(s);
    }

    public CannotEditException(long productId) {
        super(String.format("Product id %d cannot be edited because portions have been taken.",
                productId));
    }
}
