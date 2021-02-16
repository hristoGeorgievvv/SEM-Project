package nl.tudelft.sem.sem54.fridge.controller.pojo;

import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.exceptions.IncompleteRequestException;
import java.util.Date;

public class ProductEntryRequest {

    private String productName;

    private int portions;

    private int creditValue;

    private Date expirationDate;

    /**
     * Constructor for testing.
     *
     * @param productName product name
     * @param portions portions
     * @param creditValue credit value
     * @param expirationDate date of expire
     */
    public ProductEntryRequest(String productName,
                               int portions, int creditValue,
                               Date expirationDate) {
        this.productName = productName;
        this.portions = portions;
        this.creditValue = creditValue;
        this.expirationDate = expirationDate;
    }

    /**
     * Constructor for testing.
     *
     * @param product product
     */
    public ProductEntryRequest(Product product) {
        this.creditValue = product.getCreditValue();
        this.expirationDate = product.getExpirationDate();
        this.fridgeId = product.getFridge().getId();
        this.portions = product.getPortions();
        this.productName = product.getProductName();
    }


    //currently not used because we have default fridge but will be needed if we add support for multiple households.
    private long fridgeId;

    /**
     * Static method for checking if all fields are valid.
     *
     * @param productEntryRequest productEntryRequest
     */
    public static void checkValidFields(ProductEntryRequest productEntryRequest) {
        if (productEntryRequest.getProductName() == null
                || productEntryRequest.getProductName().isEmpty()
                || productEntryRequest.getCreditValue() <= 0
                || productEntryRequest.getPortions() <= 0
                || productEntryRequest.getExpirationDate() == null) {
            throw new IncompleteRequestException(
                    "The request must contain the following fields in the body: "
                            + "productName, "
                            + "portions (> 0), "
                            + "creditValue (> 0), "
                            + "expirationDate (YYYY-MM-DD)!");

        }
    }

    public int getPortions() {
        return portions;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public int getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(int creditValue) {
        this.creditValue = creditValue;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getFridgeId() {
        return fridgeId;
    }

    public void setFridgeId(long fridgeId) {
        this.fridgeId = fridgeId;
    }
}
