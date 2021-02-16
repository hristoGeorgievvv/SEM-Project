package nl.tudelft.sem.sem54.fridge.exceptions;

public class CannotUndoException extends RuntimeException {
    public static final long serialVersionUID = 432049;

    public CannotUndoException(long productId, long userId) {
        super(String.format("Cannot undo last transaction for user with id %d and product id %d",
                productId, userId));
    }

}
