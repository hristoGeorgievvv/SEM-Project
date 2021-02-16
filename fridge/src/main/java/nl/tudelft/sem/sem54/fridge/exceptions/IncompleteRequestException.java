package nl.tudelft.sem.sem54.fridge.exceptions;

public class IncompleteRequestException extends RuntimeException {
    public static final long serialVersionUID = 666666;

    public IncompleteRequestException(String s) {
        super(s);
    }

}
