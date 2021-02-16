package nl.tudelft.sem.sem54.fridge.redis.schema;

import java.util.Map;
import com.google.gson.annotations.SerializedName;
import nl.tudelft.sem.sem54.fridge.domain.Product;

/**
 * Object used for message-passing between main and fridge service.
 * We aren't using a shared library so this code should be duplicated :)
 */
public class ProductStatus {
    @SerializedName("message_type")
    private MessageType messageType;
    @SerializedName("owner_username")
    private String ownerUsername;
    @SerializedName("total_portions")
    private int totalPortions;
    @SerializedName("total_credit_value")
    private int totalCreditValue;
    @SerializedName("portions_per_user")
    private Map<String, Integer> portionsPerUser;


    /**
     * Full constructor used for testing purposes.
     *
     * @param messageType      message type
     * @param ownerUsername    username of owner
     * @param totalPortions    total portion cap of product
     * @param totalCreditValue total credit weight of product
     * @param portionsPerUser  map of portion distribution per user
     */
    public ProductStatus(MessageType messageType, String ownerUsername,
                         int totalPortions, int totalCreditValue,
                         Map<String, Integer> portionsPerUser) {
        this.messageType = messageType;
        this.ownerUsername = ownerUsername;
        this.totalPortions = totalPortions;
        this.totalCreditValue = totalCreditValue;
        this.portionsPerUser = portionsPerUser;
    }

    public static class Builder {
        private MessageType messageType;
        private String ownerUsername;
        private int totalPortions;
        private int totalCreditValue;
        private Map<String, Integer> portionsPerUser;

        public ProductStatus.Builder withMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public ProductStatus.Builder withPortionsPerUser(Map<String, Integer> portionsPerUser) {
            this.portionsPerUser = portionsPerUser;
            return this;
        }

        /**
         * Sets all fields related to product information.
         *
         * @param product product specified
         * @return ProductStatus with specified product
         */
        public ProductStatus.Builder forProduct(Product product) {
            this.ownerUsername = product.getOwner().getUsername();
            this.totalPortions = product.getPortions();
            this.totalCreditValue = product.getCreditValue();
            return this;
        }

        public ProductStatus build() {
            return new ProductStatus(this);
        }
    }

    private ProductStatus(ProductStatus.Builder b) {
        this.messageType = b.messageType;
        this.ownerUsername = b.ownerUsername;
        this.totalPortions = b.totalPortions;
        this.totalCreditValue = b.totalCreditValue;
        this.portionsPerUser = b.portionsPerUser;
    }


    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public int getTotalCreditValue() {
        return totalCreditValue;
    }

    public void setTotalCreditValue(int totalCreditValue) {
        this.totalCreditValue = totalCreditValue;
    }

    public Map<String, Integer> getPortionsPerUser() {
        return portionsPerUser;
    }

    public void setPortionsPerUser(Map<String, Integer> portionsPerUser) {
        this.portionsPerUser = portionsPerUser;
    }

    public int getTotalPortions() {
        return totalPortions;
    }

    public void setTotalPortions(int totalPortions) {
        this.totalPortions = totalPortions;
    }
}
