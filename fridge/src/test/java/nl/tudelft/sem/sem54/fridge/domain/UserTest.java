package nl.tudelft.sem.sem54.fridge.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

class UserTest {

    transient User testUser;
    transient User testUser2;

    private transient String username;
    private transient String username2;
    private transient Fridge fridge;
    private transient Fridge fridge2;

    @BeforeEach
    void beforeEach() {
        username = UUID.randomUUID().toString();
        username2 = UUID.randomUUID().toString();
        fridge = new Fridge();
        fridge2 = new Fridge();
        testUser = new User(username, fridge);
        testUser2 = new User(username2, fridge2);
    }

    @Test
    void testGetUsername() {
        assertEquals(username, testUser.getUsername());
    }

    @Test
    void testSetUsername() {
        testUser.setUsername(username2);
        assertEquals(username2, testUser.getUsername());
    }

    @Test
    void testGetFridge() {
        assertEquals(testUser.getFridge(), fridge);
    }

    @Test
    void testSetFridge() {
        testUser.setFridge(fridge2);
        assertEquals(testUser.getFridge(), fridge2);
    }

    @Test
    void testEquals_success() {
        User newUser = new User(username, new Fridge());
        assertEquals(newUser, testUser);
    }

    @Test
    void testEquals_itself() {
        assertEquals(testUser, testUser);
    }

    @Test
    void testEquals_null() {
        assertNotEquals(testUser, null);
    }

    @Test
    void testEquals_differentClass() {
        assertNotEquals(testUser, new Product());
    }
}