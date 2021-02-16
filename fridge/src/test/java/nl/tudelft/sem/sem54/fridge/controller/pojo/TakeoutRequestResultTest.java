package nl.tudelft.sem.sem54.fridge.controller.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TakeoutRequestResultTest {

    private TakeoutRequestResult takeoutRequestResult;

    @BeforeEach
    public void beforeEach() {
        takeoutRequestResult = new TakeoutRequestResult(
                TakeoutRequestStatus.SUFFICIENT, 1L, 2, 3, 4, 5);
    }

    @Test
    public void testGetProductStatus() {
        assertEquals(takeoutRequestResult.getProductStatus(), TakeoutRequestStatus.SUFFICIENT);
    }

    @Test
    public void testGetProductId() {
        assertEquals(takeoutRequestResult.getProductId(), 1L);
    }

    @Test
    public void testGetPortionsRequested() {
        assertEquals(takeoutRequestResult.getPortionsRequested(), 2);
    }

    @Test
    public void testGetPortionsLeft() {
        assertEquals(takeoutRequestResult.getPortionsLeft(), 3);
    }

    @Test
    public void testGetOutOf() {
        assertEquals(takeoutRequestResult.getOutOf(), 4);
    }

    @Test
    public void testGetDelta() {
        assertEquals(takeoutRequestResult.getDelta(), 5);
    }


    @Test
    public void testSetProductStatus() {
        takeoutRequestResult.setProductStatus(TakeoutRequestStatus.INSUFFICIENT);
        assertEquals(takeoutRequestResult.getProductStatus(), TakeoutRequestStatus.INSUFFICIENT);
    }

    @Test
    public void testSetProductId() {
        takeoutRequestResult.setProductId(42L);
        assertEquals(takeoutRequestResult.getProductId(), 42L);
    }

    @Test
    public void testSetPortionsRequested() {
        takeoutRequestResult.setPortionsRequested(42);
        assertEquals(takeoutRequestResult.getPortionsRequested(), 42);
    }

    @Test
    public void testSetPortionsLeft() {
        takeoutRequestResult.setPortionsLeft(42);
        assertEquals(takeoutRequestResult.getPortionsLeft(), 42);
    }

    @Test
    public void testSetOutOf() {
        takeoutRequestResult.setOutOf(42);
        assertEquals(takeoutRequestResult.getOutOf(), 42);
    }

    @Test
    public void testSetDelta() {
        takeoutRequestResult.setDelta(42);
        assertEquals(takeoutRequestResult.getDelta(), 42);
    }
}
