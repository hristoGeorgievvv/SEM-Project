package nl.tudelft.sem.sem54.fridge.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProductTest {

    private transient Product product;

    private transient String productName;
    private transient int portions;
    private transient int portionsLeft;
    private transient int creditValue;
    private transient Date expirationDate;
    private transient Fridge fridge;

    /**
     * Setup variables for the testing environment.
     */

    @BeforeEach
    public void beforeEach() {
        productName = UUID.randomUUID().toString();
        portions = ThreadLocalRandom.current().nextInt();
        portionsLeft = ThreadLocalRandom.current().nextInt();
        creditValue = ThreadLocalRandom.current().nextInt();
        expirationDate = new Date(ThreadLocalRandom.current().nextInt() * 1000L);
        fridge = new Fridge();
        product = new Product(productName, portions, portionsLeft, 10, expirationDate, fridge);
        product.setId(42);
    }

    @Test
    void testBuilder() {

        User user = new User();

        Product product = new Product.Builder()
                .setProductName(productName)
                .setOwner(user)
                .setPortions(portions)
                .setPortionsLeft(portions)
                .setCreditValue(creditValue)
                .setExpirationDate(expirationDate)
                .setFridge(fridge)
                .build();

        Product product1 = new Product(productName, user,
                portions,
                creditValue,
                expirationDate,
                fridge);

        assertEquals(product, product1);
    }


    @Test
    void testGetProductName() {
        assertEquals(product.getProductName(), productName);
    }

    @Test
    void testSetProductName() {
        productName = UUID.randomUUID().toString();
        product.setProductName(productName);
        assertEquals(product.getProductName(), productName);
    }

    @Test
    void testGetPortions() {
        assertEquals(product.getPortions(), portions);
    }

    @Test
    void testSetPortions() {
        portions = ThreadLocalRandom.current().nextInt();
        product.setPortions(portions);
        assertEquals(product.getPortions(), portions);
    }

    @Test
    void testGetPortionsLeft() {
        assertEquals(product.getPortionsLeft(), portionsLeft);
    }

    @Test
    void testSetPortionsLeft() {
        portionsLeft = ThreadLocalRandom.current().nextInt();
        product.setPortionsLeft(portionsLeft);
        assertEquals(product.getPortionsLeft(), portionsLeft);
    }

    @Test
    void testGetExpirationDate() {
        assertEquals(product.getExpirationDate(), expirationDate);
    }

    @Test
    void testSetExpirationDate() {
        expirationDate = new Date(ThreadLocalRandom.current().nextInt() * 1000L);
        product.setExpirationDate(expirationDate);
        assertEquals(product.getExpirationDate(), expirationDate);
    }

    @Test
    void testGetFridge() {
        assertEquals(product.getFridge(), fridge);
    }

    @Test
    void testSetFridge() {
        fridge = new Fridge();
        product.setFridge(fridge);
        assertEquals(product.getFridge(), fridge);
    }

    @Test
    void testGetId() {
        assertEquals(product.getId(), 42);
    }

    @Test
    void testSetId() {
        product.setId(43);
        assertEquals(product.getId(), 43);
    }

    @Test
    void testGetTransactionList() {
        assertTrue(product.getTransactionList() == null || product.getTransactionList().isEmpty());
    }

    @Test
    void testSetTransactionList() {
        ProductTransaction t1 = new ProductTransaction(product, 3);
        ProductTransaction t2 = new ProductTransaction(product, 5);
        product.setTransactionList(Arrays.asList(t1, t2));

        assertTrue(product.getTransactionList().contains(t1));
        assertTrue(product.getTransactionList().contains(t2));
    }

    @Test
    void testGetUserPortionMap_userNotContained() {
        User u1 = new User();
        u1.setUsername("u1");

        User u2 = new User();
        u2.setUsername("u2");

        int t1Portions = 4;

        ProductTransaction t1 = new ProductTransaction(new Timestamp(42), u1,
                product, 0, t1Portions, false, null);
        product.setTransactionList(Collections.singletonList(t1));



    }

    @Test
    void testGetOwner() {
        User owner = new User();
        owner.setId(1337);
        product.setOwner(owner);
        assertEquals(product.getOwner(), owner);
    }


    @Test
    void testGetCreditValue() {
        assertEquals(product.getCreditValue(), 10);
    }

    @Test
    void testSetCreditValue() {
        product.setCreditValue(42);
        assertEquals(product.getCreditValue(), 42);
    }

    static class ProductEqualsTest {
        private Product product1;
        private Product product2;

        @BeforeEach
        public void beforeEach() {
            Fridge fridge1 = new Fridge();
            product1 = new Product("apple", 1, 1, 1, new Date(System.currentTimeMillis()), fridge1);
            product2 = new Product("apple", 1, 1, 1, new Date(System.currentTimeMillis()), fridge1);
            fridge1.setProducts(Arrays.asList(product1, product2));
        }

        @Test
        void testEquals_success() {
            assertEquals(product1, product2);
        }

        @Test
        void testEquals_itself() {
            assertEquals(product1, product1);
        }

        @Test
        void testEquals_null() {
            assertNotEquals(product1, null);
        }

        @Test
        void testEquals_differentClass() {
            assertNotEquals(product1, new User());
        }

        @Test
        void testEquals_differentPortions() {
            product2.setPortions(product1.getPortions() + 1);
            assertNotEquals(product1, product2);
        }

        @Test
        void testEquals_differentPortionsLeft() {
            product2.setPortionsLeft(product1.getPortionsLeft() + 1);
            assertNotEquals(product1, product2);
        }

        @Test
        void testEquals_differentExpirationDate() {
            product2.setExpirationDate(new Date(product1.getExpirationDate().getTime() + 1));
            assertNotEquals(product1, product2);
        }

        @Test
        void testEquals_differentFridge() {
            product2.setFridge(new Fridge());
            assertNotEquals(product1, product2);
        }

        @Test
        void testEquals_differentName() {
            product2.setProductName(product1.getProductName() + "x");
            assertNotEquals(product1, product2);
        }

    }
}
