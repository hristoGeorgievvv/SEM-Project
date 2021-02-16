package nl.tudelft.sem.sem54.fridge.exceptions;

public class NotEnoughPortionsException extends RuntimeException {
    public static final long serialVersionUID = 432053;

    /**
     * Create a runtimeException.
     *
     * @param required The required amount of portions
     * @param available The available amount of portions
     * @param productId The product ID (for display purposes)
     */
    public NotEnoughPortionsException(int required, int available, long productId) {
        super("There are not enough portions of product " + productId
                + ". There are only " + available + " portions available while there are "
                + required + " requested.");
    }
}