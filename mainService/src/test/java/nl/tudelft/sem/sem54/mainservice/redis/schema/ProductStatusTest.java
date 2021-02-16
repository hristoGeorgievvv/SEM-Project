package nl.tudelft.sem.sem54.mainservice.redis.schema;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductStatusTest {

    private ProductStatus productStatus;
    private Map<String, Integer> map;

    @BeforeEach
    void beforeEach() {
        map = new HashMap<>();
        productStatus = new ProductStatus(MessageType.PRODUCT_FINISHED, "owner",
            12, 13, map);
    }

    @Test
    void testGetMessageType() {
        assertThat(productStatus.getMessageType())
            .isEqualByComparingTo(MessageType.PRODUCT_FINISHED);
    }

    @Test
    void testSetMessageType() {
        productStatus.setMessageType(MessageType.PRODUCT_REMOVED);
        assertThat(productStatus.getMessageType())
            .isEqualByComparingTo(MessageType.PRODUCT_REMOVED);
    }

    @Test
    void testGetOwnerUsername() {
        assertThat(productStatus.getOwnerUsername()).isEqualTo("owner");
    }

    @Test
    void testSetOwnerUsername() {
        productStatus.setOwnerUsername("bob");
        assertThat(productStatus.getOwnerUsername()).isEqualTo("bob");
    }

    @Test
    void testGetTotalCreditValue() {
        assertThat(productStatus.getTotalCreditValue()).isEqualTo(13);
    }

    @Test
    void testSetTotalCreditValue() {
        productStatus.setTotalCreditValue(14);
        assertThat(productStatus.getTotalCreditValue()).isEqualTo(14);
    }

    @Test
    void testGetPortionsPerUser() {
        assertThat(productStatus.getPortionsPerUser()).isSameAs(map);
    }

    @Test
    void testSetPortionsPerUser() {
        HashMap<String, Integer> newMap = new HashMap<>();
        productStatus.setPortionsPerUser(newMap);
        assertThat(productStatus.getPortionsPerUser()).isSameAs(newMap);
    }

    @Test
    void testGetTotalPortions() {
        assertThat(productStatus.getTotalPortions()).isEqualTo(12);
    }

    @Test
    void testSetTotalPortions() {
        productStatus.setTotalPortions(1342);
        assertThat(productStatus.getTotalPortions()).isEqualTo(1342);
    }

    @Test
    void testToString() {
        assertThat(productStatus.toString()).isEqualTo("ProductStatus{"
            + "messageType=PRODUCT_FINISHED, "
            + "ownerUsername='owner, "
            + "totalPortions=12, "
            + "totalCreditValue=13, "
            + "portionsPerUser={}}");
    }

    @Test
    void testEquals_itself() {
        assertThat(productStatus).isEqualTo(productStatus);
    }

    @Test
    void testEquals_null() {
        assertThat(productStatus).isNotEqualTo(null);
    }

    @Test
    void testEquals_otherClass() {
        assertThat(productStatus).isNotEqualTo(new Object());
    }

    @Test
    void testEquals_empty() {
        ProductStatus other = new ProductStatus();
        assertThat(productStatus).isNotEqualTo(other);
    }

    @Test
    void testEquals_success() {
        ProductStatus other = new ProductStatus(productStatus.getMessageType(),
            productStatus.getOwnerUsername(),
            productStatus.getTotalPortions(),
            productStatus.getTotalCreditValue(),
            productStatus.getPortionsPerUser());
        assertThat(productStatus).isEqualTo(other);
    }

    @Test
    void testEquals_differentOwnerUsername() {
        ProductStatus other = new ProductStatus(productStatus.getMessageType(),
            productStatus.getOwnerUsername() + "nonono",
            productStatus.getTotalPortions(),
            productStatus.getTotalCreditValue(),
            productStatus.getPortionsPerUser());
        assertThat(productStatus).isNotEqualTo(other);
    }

    @Test
    void testEquals_differentProductStatus() {
        ProductStatus other = new ProductStatus(MessageType.PRODUCT_SPOILED,
            productStatus.getOwnerUsername(),
            productStatus.getTotalPortions(),
            productStatus.getTotalCreditValue(),
            productStatus.getPortionsPerUser());
        assertThat(productStatus).isNotEqualTo(other);
    }

    @Test
    void testEquals_differentPortionsPerUser() {
        HashMap<String, Integer> portionsPerUser = new HashMap<>();
        portionsPerUser.put("clyde", 2);
        ProductStatus other = new ProductStatus(productStatus.getMessageType(),
            productStatus.getOwnerUsername(),
            productStatus.getTotalPortions(),
            productStatus.getTotalCreditValue(),
            portionsPerUser);
        assertThat(productStatus).isNotEqualTo(other);
    }

    @Test
    void testEquals_differentTotalPortions() {
        ProductStatus other = new ProductStatus(productStatus.getMessageType(),
            productStatus.getOwnerUsername(),
            productStatus.getTotalPortions() + 1,
            productStatus.getTotalCreditValue(),
            productStatus.getPortionsPerUser());
        assertThat(productStatus).isNotEqualTo(other);
    }

    @Test
    void testEquals_differentTotalCreditValue() {
        ProductStatus other = new ProductStatus(productStatus.getMessageType(),
            productStatus.getOwnerUsername(),
            productStatus.getTotalPortions(),
            productStatus.getTotalCreditValue() + 1,
            productStatus.getPortionsPerUser());
        assertThat(productStatus).isNotEqualTo(other);
    }
}