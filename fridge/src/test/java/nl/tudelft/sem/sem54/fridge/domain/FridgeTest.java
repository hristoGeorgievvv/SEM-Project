package nl.tudelft.sem.sem54.fridge.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.sem54.fridge.domain.utils.DomainUtils;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

public class FridgeTest {

    private transient Fridge fridge;
    private transient List<User> users;
    private transient List<Product> products;

    @BeforeEach
    void beforeEach() {
        users =  DomainUtils.createUserList();
        products = DomainUtils.createProductList();
        fridge = new Fridge(users, products);
    }

    @Test
    void testGetUsers() {
        assertEquals(fridge.getUsers(), users);
    }

    @Test
    void testSetUsers() {
        users =  DomainUtils.createUserList();
        fridge.setUsers(users);
        assertEquals(fridge.getUsers(), users);
    }

    @Test
    void testGetProducts() {
        assertEquals(fridge.getProducts(), products);
    }

    @Test
    void testSetProducts() {
        products =  DomainUtils.createProductList();
        fridge.setProducts(products);
        assertEquals(fridge.getProducts(), products);
    }

    @Test
    void testAddProduct() {
        Product p1 = new Product();
        p1.setId(42);

        fridge.setProducts(new ArrayList<>());
        fridge.addProduct(p1);

        assertTrue(fridge.getProducts().contains(p1));
    }

    @Test
    void testRemoveProduct_containedInFridge() {
        Product p1 = new Product();
        p1.setId(42);

        List<Product> productList = new ArrayList<>();
        productList.add(p1);
        fridge.setProducts(productList);

        fridge.removeProduct(p1);
        assertTrue(fridge.getProducts().isEmpty());

    }

    @Test
    void testRemoveProduct_notContainedInFridge() {
        Product p1 = new Product();
        p1.setId(42);

        Product p2 = new Product();
        p2.setId(43);

        List<Product> productList = new ArrayList<>();
        productList.add(p1);
        fridge.setProducts(productList);

        assertThrows(ProductNotFoundException.class, () -> fridge.removeProduct(p2));
    }

    @Test
    void testGetExpiredProducts() {
        Product expiredProduct = new Product();
        expiredProduct.setExpirationDate(new Date(System.currentTimeMillis() - 1));

        Product freshProduct = new Product();
        freshProduct.setExpirationDate(new Date(Long.MAX_VALUE));

        fridge.setProducts(Arrays.asList(expiredProduct, freshProduct));

        assertFalse(fridge.getExpiredProducts().contains(freshProduct));
        assertTrue(fridge.getExpiredProducts().contains(expiredProduct));
    }

    @Test
    void testGetEatenProducts() {
        Product eatenProduct = new Product();
        eatenProduct.setPortionsLeft(0);


        Product notEatenProduct = new Product();
        notEatenProduct.setPortionsLeft(1);

        fridge.setProducts(Arrays.asList(eatenProduct, notEatenProduct));

        assertTrue(fridge.getEatenProducts().contains(eatenProduct));
        assertFalse(fridge.getEatenProducts().contains(notEatenProduct));
    }

    @Test
    void testGetAvailableProducts() {
        Product eatenFreshProduct = new Product();
        eatenFreshProduct.setPortionsLeft(0);
        eatenFreshProduct.setExpirationDate(new Date(Long.MAX_VALUE));

        Product remainingFreshProduct = new Product();
        remainingFreshProduct.setPortionsLeft(1);
        remainingFreshProduct.setExpirationDate(new Date(Long.MAX_VALUE));

        Product eatenExpiredProduct = new Product();
        eatenExpiredProduct.setPortionsLeft(0);
        eatenExpiredProduct.setExpirationDate(new Date(System.currentTimeMillis() - 1));

        fridge.setProducts(Arrays.asList(remainingFreshProduct, eatenExpiredProduct, eatenFreshProduct));

        assertTrue(fridge.getAvailableProducts().contains(remainingFreshProduct));
        assertFalse(fridge.getAvailableProducts().contains(eatenExpiredProduct));
        assertFalse(fridge.getAvailableProducts().contains(eatenFreshProduct));

    }

    static class FridgeEqualsTest {

        private List<Product> productList;
        private List<User> userList;
        private Fridge fridge1;
        private Fridge fridge2;

        @BeforeEach
        public void beforeEach() {
            userList = Collections.singletonList(new User("a"));
            productList = Collections.singletonList(new Product());
            fridge1 = new Fridge(userList, productList);
            fridge2 = new Fridge(userList, productList);
        }

        @Test
        void testEquals_success() {
            assertEquals(fridge1, fridge2);
        }

        @Test
        void testEquals_itself() {
            assertEquals(fridge1, fridge1);
        }

        @Test
        void testEquals_null() {
            assertNotEquals(fridge1, null);
        }

        @Test
        void testEquals_differentClass() {
            assertNotEquals(fridge1, new User());
        }

        @Test
        void testEquals_differentUsers() {
            fridge1.setUsers(Collections.singletonList(new User("b")));
            assertNotEquals(fridge1, fridge2);
        }

        @Test
        void testEquals_differentProducts() {
            Product product1 = new Product();
            product1.setProductName("apple");
            fridge1.setProducts(Collections.singletonList(product1));
            assertNotEquals(fridge1, fridge2);
        }

        @Test
        void testHashCode() {
            Fridge fridge1 = new Fridge();
            fridge1.setUsers(userList);
            fridge1.setProducts(productList);

            Fridge fridge2 = new Fridge();
            fridge2.setUsers(userList);
            fridge2.setProducts(productList);

            assertTrue(fridge1.equals(fridge2) && fridge2.equals(fridge1));
            assertEquals(fridge2.hashCode(), fridge1.hashCode());
        }
    }

    @Nested
    class FridgeTransactionValidationTest {
        private transient User user;
        private transient Product product;
        private transient ProductTransaction pt;
        private transient ProductTransaction ptUndo;

        @BeforeEach
        void beforeEach() {
            product = new Product();
            product.setId(100);

            user = new User();
            user.setId(42);

            product = new Product();
            product.setId(100);
            product.setPortionsLeft(10);

            fridge = new Fridge();
            fridge.setProducts(Collections.singletonList(product));
            fridge.setUsers(Collections.singletonList(user));

            // pt is a correct ProductTransaction.
            // We will change one field at a time to test each logic check robustly.
            pt = new ProductTransaction();
            pt.setId(1);
            pt.setTimestamp(new Timestamp(42));
            pt.setPortions(10);
            pt.setUser(user);
            pt.setProduct(product);
            pt.setRevert(false);

            // ptUndo is a correct ProductTransaction that undoes pt.
            // We will change one field at a time to test each logic check robustly.
            ptUndo = new ProductTransaction();
            ptUndo.setId(pt.getId() + 1);
            ptUndo.setTimestamp(new Timestamp(pt.getTimestamp().getTime() + 1));
            ptUndo.setPortions(-pt.getPortions());
            ptUndo.setUser(user);
            ptUndo.setProduct(product);
            ptUndo.setRevert(true);
            ptUndo.setRevertedProductTransaction(pt);

            product.setTransactionList(Arrays.asList(pt, ptUndo));
        }

        @Test
        void testIsTransactionValid_valid() {
            assertTrue(fridge.isTransactionValid(pt));
        }

        @Test
        void testIsTransactionValid_validUndo() {
            assertTrue(fridge.isTransactionValid(ptUndo));
        }

        @Test
        void testIsTransactionValid_wrongProduct() {
            Product wrongProduct = new Product();
            wrongProduct.setId(product.getId() + 1);

            pt.setProduct(wrongProduct);
            assertFalse(fridge.isTransactionValid(pt));
        }

        @Test
        void testIsTransactionValid_wrongUser() {
            User wrongUser = new User();
            wrongUser.setId(user.getId() + 1);

            pt.setUser(wrongUser);
            assertFalse(fridge.isTransactionValid(pt));

        }

        @Test
        void testIsTransactionValid_wrongPortions() {
            pt.setPortions(product.getPortionsLeft() + 1);
            assertFalse(fridge.isTransactionValid(pt));
        }

        @Test
        void testIsTransactionValid_negativePortions() {
            pt.setPortions(-product.getPortionsLeft());
            assertFalse(fridge.isTransactionValid(pt));

        }

        @Test
        void testIsTransactionValid_undoesWrongTransaction() {
            ptUndo.setRevertedProductTransaction(new ProductTransaction());
            assertFalse(fridge.isTransactionValid(ptUndo));

        }

        @Test
        void testIsTransactionValid_undoesWrongPortions() {
            ptUndo.setPortions(pt.getPortions() + 1);
            assertFalse(fridge.isTransactionValid(ptUndo));
        }

        @Test
        void testIsTransactionValid_undoesFutureTransaction() {
            ptUndo.setTimestamp(new Timestamp(pt.getTimestamp().getTime() - 1));
            assertFalse(fridge.isTransactionValid(ptUndo));
        }
    }

}
