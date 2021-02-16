package nl.tudelft.sem.sem54.fridge.controller.pojo;

public class RemoveProductRequest {
    private long productId;

    public RemoveProductRequest(long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
