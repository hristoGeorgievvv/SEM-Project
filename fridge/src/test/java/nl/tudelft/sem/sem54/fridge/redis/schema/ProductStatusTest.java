package nl.tudelft.sem.sem54.fridge.redis.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

public class ProductStatusTest {

    private HashMap<String, Integer> map;
    private ProductStatus productStatus;

    @BeforeEach
    public void beforeEach() {
        map = new HashMap<>();
        productStatus = new ProductStatus(MessageType.PRODUCT_FINISHED, "admin", 1, 2, map);
    }

    @Test
    public void testGetMessageType() {
        assertEquals(productStatus.getMessageType(), MessageType.PRODUCT_FINISHED);
    }

    @Test
    public void testGetOwnerUsername() {
        assertEquals(productStatus.getOwnerUsername(), "admin");
    }

    @Test
    public void testGetTotalCreditValue() {
        assertEquals(productStatus.getTotalCreditValue(), 2);
    }

    @Test
    public void testGetTotalPortions() {
        assertEquals(productStatus.getTotalPortions(), 1);
    }

    @Test
    public void testGetPortionsPerUser() {
        assertEquals(productStatus.getPortionsPerUser(), map);
    }

    @Test
    public void testSetMessageType() {
        productStatus.setMessageType(MessageType.PRODUCT_SPOILED);
        assertEquals(productStatus.getMessageType(), MessageType.PRODUCT_SPOILED);
    }

    @Test
    public void testSetOwnerUsername() {
        productStatus.setOwnerUsername("alice");
        assertEquals(productStatus.getOwnerUsername(), "alice");
    }

    @Test
    public void testSetTotalCreditValue() {
        productStatus.setTotalCreditValue(42);
        assertEquals(productStatus.getTotalCreditValue(), 42);
    }

    @Test
    public void testSetTotalPortions() {
        productStatus.setTotalPortions(42);
        assertEquals(productStatus.getTotalPortions(), 42);
    }

    @Test
    public void testSetPortionsPerUser() {
        HashMap<String, Integer> newMap = new HashMap<>();
        newMap.put("a", 1);
        productStatus.setPortionsPerUser(newMap);
        assertEquals(productStatus.getPortionsPerUser(), newMap);
    }


}
