package nl.tudelft.sem.sem54.fridge.controller.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoveProductRequestTest {

    private RemoveProductRequest removeProductRequest;

    @BeforeEach
    public void beforeEach() {
        removeProductRequest = new RemoveProductRequest(1L);
    }

    @Test
    public void testGetProductId() {
        assertEquals(removeProductRequest.getProductId(), 1L);
    }

    @Test
    public void testSetProductId() {
        removeProductRequest.setProductId(42L);
        assertEquals(removeProductRequest.getProductId(), 42L);
    }
}
