package nl.tudelft.sem.sem54.fridge.exceptions;

public class UserNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 432052;

    public UserNotFoundException() {
        super("User not found exception");
    }
}
