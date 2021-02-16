package nl.tudelft.sem.sem54.fridge.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;

public class ProductTransactionTest {
    private ProductTransaction productTransaction;

    @BeforeEach
    void beforeEach() {
        Product product = new Product();
        product.setProductName("nargel");
        User user = new User("ahmed");
        productTransaction = new ProductTransaction(new Timestamp(42), user, product, 1, 2, true, null);
        productTransaction.setRevertedProductTransaction(productTransaction); // inception
        productTransaction.setId(3);
    }

    @Test
    void testBuilder() {

        Timestamp timestamp = new Timestamp(10000);
        User user = new User();
        Product product = new Product();
        int credits = ThreadLocalRandom.current().nextInt();
        int portions = ThreadLocalRandom.current().nextInt();
        boolean isRevert = true;
        ProductTransaction productTransactionUndo = mock(ProductTransaction.class);

        ProductTransaction productTransaction = new ProductTransaction.Builder()
                .setTimestamp(timestamp)
                .setUser(user)
                .setProduct(product)
                .setCredits(credits)
                .setPortions(portions)
                .setRevert(isRevert)
                .setProductTransaction(productTransactionUndo)
                .build();


        assertEquals(productTransaction.getUser(), user);
        assertEquals(productTransaction.getTimestamp(), timestamp);
        assertEquals(productTransaction.getProduct(), product);
        assertEquals(productTransaction.getCredits(), credits);
        assertEquals(productTransaction.getPortions(), portions);
        assertEquals(productTransaction.getIsRevert(), isRevert);
        assertEquals(productTransaction.getRevertedProductTransaction(), productTransactionUndo);
    }

    @Test
    void testGetUser() {
        assertEquals(productTransaction.getUser().getUsername(), "ahmed");
    }

    @Test
    void testGetProduct() {
        assertEquals(productTransaction.getProduct().getProductName(), "nargel");
    }

    @Test
    void testGetId() {
        assertEquals(productTransaction.getId(), 3);
    }

    @Test
    void testGetTimestamp() {
        assertEquals(productTransaction.getTimestamp().getTime(), 42);
    }

    @Test
    void testGetCredits() {
        assertEquals(productTransaction.getCredits(), 1);
    }

    @Test
    void testGetPortions() {
        assertEquals(productTransaction.getPortions(), 2);
    }

    @Test
    void testGetIsRevert() {
        assertTrue(productTransaction.getIsRevert());
    }

    @Test
    void testGetRevertedProductTransaction() {
        assertEquals(
                productTransaction.getRevertedProductTransaction(), productTransaction);
    }

    @Test
    void testSetUser() {
        productTransaction.setUser(new User("abdul"));
        assertEquals(productTransaction.getUser().getUsername(), "abdul");
    }

    @Test
    void testSetProduct() {
        Product p1 = new Product();
        p1.setProductName("tutun");
        productTransaction.setProduct(p1);
        assertEquals(productTransaction.getProduct().getProductName(), "tutun");
    }

    @Test
    void testSetId() {
        productTransaction.setId(42);
        assertEquals(productTransaction.getId(), 42);
    }

    @Test
    void testSetTimestamp() {
        productTransaction.setTimestamp(new Timestamp(43));
        assertEquals(productTransaction.getTimestamp().getTime(), 43);
    }

    @Test
    void testSetCredits() {
        productTransaction.setCredits(42);
        assertEquals(productTransaction.getCredits(), 42);
    }

    @Test
    void testSetPortions() {
        productTransaction.setPortions(42);
        assertEquals(productTransaction.getPortions(), 42);
    }

    @Test
    void testSetIsRevert() {
        productTransaction.setRevert(false);
        assertFalse(productTransaction.getIsRevert());
    }

    @Test
    void testSetRevertedProductTransaction() {
        productTransaction.setRevertedProductTransaction(null);
        assertNull(productTransaction.getRevertedProductTransaction());
    }
}
