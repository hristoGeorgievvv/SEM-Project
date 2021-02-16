package nl.tudelft.sem.sem54.fridge.controller.pojo;

public class TakeoutRequest {
    private String username;
    private long productId;
    private int amount;

    /**
     * Constructor for testing purposes.
     *
     * @param productId productId
     * @param amount    amount
     */
    public TakeoutRequest(long productId, int amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public TakeoutRequest() {
    }

    /**
     * Constructor for testing purposes.
     *
     * @param productId productId
     * @param amount    amount
     */
    public TakeoutRequest(String username, long productId, int amount) {
        this.username = username;
        this.productId = productId;
        this.amount = amount;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
