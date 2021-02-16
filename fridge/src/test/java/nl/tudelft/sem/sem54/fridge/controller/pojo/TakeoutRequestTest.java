package nl.tudelft.sem.sem54.fridge.controller.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TakeoutRequestTest {
    private TakeoutRequest takeoutRequest;

    @BeforeEach
    public void beforeEach() {
        takeoutRequest = new TakeoutRequest(1L, 10);
    }

    @Test
    public void testGetProductId() {
        assertEquals(takeoutRequest.getProductId(), 1L);
    }

    @Test
    public void testSetProductId() {
        takeoutRequest.setProductId(42);
        assertEquals(takeoutRequest.getProductId(), 42);
    }

    @Test
    public void testGetAmount() {
        assertEquals(takeoutRequest.getAmount(), 10);
    }

    @Test
    public void testSetAmount() {
        takeoutRequest.setAmount(42);
        assertEquals(takeoutRequest.getAmount(), 42);
    }
}
