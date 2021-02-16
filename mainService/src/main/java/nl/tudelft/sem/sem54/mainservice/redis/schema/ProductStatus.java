package nl.tudelft.sem.sem54.mainservice.redis.schema;

import java.util.Map;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;


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
     * (Replace this with a builder pattern)
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


    public ProductStatus() {
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

    @Override
    public String toString() {
        return "ProductStatus{"
            + "messageType=" + messageType
            + ", ownerUsername='" + ownerUsername
            + ", totalPortions=" + totalPortions
            + ", totalCreditValue=" + totalCreditValue
            + ", portionsPerUser=" + portionsPerUser
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductStatus that = (ProductStatus) o;
        return totalPortions == that.totalPortions
            && totalCreditValue == that.totalCreditValue
            && messageType == that.messageType
            && Objects.equals(ownerUsername, that.ownerUsername)
            && Objects.equals(portionsPerUser, that.portionsPerUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, ownerUsername,
            totalPortions, totalCreditValue, portionsPerUser);
    }
}
