package nl.tudelft.sem.sem54.mainservice.exceptions;

public class PortionsDoNotFitException extends Exception {
    private static final long serialVersionUID = 1;

    public PortionsDoNotFitException() {
        super();
    }

    public PortionsDoNotFitException(String message) {
        super(message);
    }
}
