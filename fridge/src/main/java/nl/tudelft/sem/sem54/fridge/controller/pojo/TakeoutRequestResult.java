package nl.tudelft.sem.sem54.fridge.controller.pojo;

import nl.tudelft.sem.sem54.fridge.domain.Product;

public class TakeoutRequestResult {
    /**
     * Indicates product status after operation.
     */
    private TakeoutRequestStatus takeoutRequestStatus;

    /**
     * Product key.
     */
    private long productId;

    /**
     * Portions requested in inbound request.
     */
    private int portionsRequested;

    /**
     * Portions left after request has been processed.
     */
    private int portionsLeft;

    /**
     * Product portion limit.
     */
    private int outOf;

    /**
     * Portion difference after request.
     */
    private int delta;


    public TakeoutRequestStatus getProductStatus() {
        return takeoutRequestStatus;
    }

    public void setProductStatus(TakeoutRequestStatus takeoutRequestStatus) {
        this.takeoutRequestStatus = takeoutRequestStatus;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getPortionsRequested() {
        return portionsRequested;
    }

    public void setPortionsRequested(int portionsRequested) {
        this.portionsRequested = portionsRequested;
    }

    public int getPortionsLeft() {
        return portionsLeft;
    }

    public void setPortionsLeft(int portionsLeft) {
        this.portionsLeft = portionsLeft;
    }

    public int getOutOf() {
        return outOf;
    }

    public void setOutOf(int outOf) {
        this.outOf = outOf;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    /**
     * Testing constructor of TakeOutRequestResult.
     *
     * @param takeoutRequestStatus the status of the request.
     * @param productId            the id of the taken product.
     * @param portionsRequested    the number of portions the user wants to take.
     * @param portionsLeft         the number of portions that are left.
     * @param outOf                the limit of portions one can take.
     * @param delta                the absolute change in portions after the request is fulfilled.
     */
    public TakeoutRequestResult(TakeoutRequestStatus takeoutRequestStatus, long productId,
                                int portionsRequested, int portionsLeft, int outOf, int delta) {
        this.takeoutRequestStatus = takeoutRequestStatus;
        this.productId = productId;
        this.portionsRequested = portionsRequested;
        this.portionsLeft = portionsLeft;
        this.outOf = outOf;
        this.delta = delta;
    }

    public static class Builder {
        private TakeoutRequestStatus takeoutRequestStatus;
        private long productId;
        private int portionsRequested;
        private int portionsLeft;
        private int outOf;
        private int delta;

        public TakeoutRequestResult.Builder withStatus(TakeoutRequestStatus takeoutRequestStatus) {
            this.takeoutRequestStatus = takeoutRequestStatus;
            return this;
        }

        public TakeoutRequestResult.Builder withDelta(int delta) {
            this.delta = delta;
            return this;
        }

        /**
         * Sets all fields related to product information.
         *
         * @param product product specified
         * @return TakeoutRequestResult with specified product
         */
        public TakeoutRequestResult.Builder forProduct(Product product) {
            this.productId = product.getId();
            this.portionsLeft = product.getPortionsLeft();
            this.outOf = product.getPortions();
            return this;
        }

        public TakeoutRequestResult.Builder responseTo(TakeoutRequest takeoutRequest) {
            this.portionsRequested = takeoutRequest.getAmount();
            return this;
        }

        public TakeoutRequestResult.Builder setAmount(int amount) {
            this.portionsLeft = amount;
            return this;
        }


        public TakeoutRequestResult build() {
            return new TakeoutRequestResult(this);
        }
    }

    private TakeoutRequestResult(TakeoutRequestResult.Builder b) {
        this.takeoutRequestStatus = b.takeoutRequestStatus;
        this.productId = b.productId;
        this.portionsRequested = b.portionsRequested;
        this.portionsLeft = b.portionsLeft;
        this.outOf = b.outOf;
        this.delta = b.delta;
    }

}