package nl.tudelft.sem.sem54.mainservice.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    transient UserEntity testUser;

    private transient String username;
    private transient String username2;
    private transient float credits;
    private transient float credits2;

    @BeforeEach
    void beforeEach() {
        username = UUID.randomUUID().toString();
        username2 = UUID.randomUUID().toString();
        credits = -100 + ThreadLocalRandom.current().nextFloat() * 200;
        credits2 = -100 + ThreadLocalRandom.current().nextFloat() * 200;
        testUser = new UserEntity(username, credits);
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
    void testGetPassword() {
        assertEquals(credits, testUser.getCredits());
    }

    @Test
    void testSetPassword() {
        testUser.setCredits(credits2);
        assertEquals(credits2, testUser.getCredits());
    }

    @Test
    void testToString() {
        String theString = testUser.toString();
        assertEquals("UserEntity{"
            + "username='" + username + '\''
            + ", credits='" + credits + '\''
            + '}', theString);
    }

    @Test
    void testEquals_success() {
        UserEntity newUser = new UserEntity(username, credits2);
        assertEquals(testUser, newUser);
    }

    @Test
    void testEquals_null() {
        UserEntity newUser = null;
        assertNotEquals(testUser, newUser);
    }

    @Test
    void testEquals_differentClass() {
        Object newUser = new ArrayList();
        assertNotEquals(testUser, newUser);
    }

    @Test
    void testGetFlagged_atBoundaryFalse() {
        testUser.setCredits(-50.0f);
        assertFalse(testUser.getFlagged());
    }

    @Test
    void testFlaggedTrue_atBoundaryTrue() {
        testUser.setCredits(-51.0f);
        assertTrue(testUser.getFlagged());
    }
}