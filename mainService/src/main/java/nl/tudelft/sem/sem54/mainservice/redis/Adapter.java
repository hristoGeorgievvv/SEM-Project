package nl.tudelft.sem.sem54.mainservice.redis;

public interface Adapter {
    /**
     * Process a redis message in json.
     *
     * @param json the imput json message
     */
    void process(String json);
}
