package nl.tudelft.sem.sem54.fridge.controller.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class ProductEntryRequestTest {
    private ProductEntryRequest req;

    @BeforeEach
    public void beforeEach() {
        req = new ProductEntryRequest("bahur", 10, 10, new Date(System.currentTimeMillis()));
    }

    @Test
    public void testSetProductName() {
        req.setProductName("apple");
        assertEquals(req.getProductName(), "apple");
    }

    @Test
    public void testSetPortions() {
        req.setPortions(20);
        assertEquals(req.getPortions(), 20);
    }

    @Test
    public void testSetCreditValue() {
        req.setCreditValue(20);
        assertEquals(req.getCreditValue(), 20);
    }

    @Test
    public void testSetExpirationDate() {
        req.setExpirationDate(new Date(42));
        assertEquals(req.getExpirationDate().getTime(), 42);
    }

    @Test
    public void testSetFridgeId() {
        req.setFridgeId(42);
        assertEquals(req.getFridgeId(), 42);
    }


}