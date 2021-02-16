package nl.tudelft.sem.sem54.fridge.redis;

public interface MessagePublisher {
    void publish(String message);
}