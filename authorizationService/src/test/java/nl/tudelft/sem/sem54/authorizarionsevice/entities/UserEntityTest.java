package nl.tudelft.sem.sem54.authorizarionsevice.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    transient UserEntity testUser;

    private transient String username;
    private transient String username2;
    private transient String password;
    private transient String password2;

    @BeforeEach
    void beforeEach() {
        username = UUID.randomUUID().toString();
        username2 = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString();
        password2 = UUID.randomUUID().toString();
        testUser = new UserEntity(username, password);
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
        assertEquals(password, testUser.getPassword());
    }

    @Test
    void testSetPassword() {
        testUser.setPassword(password2);
        assertEquals(password2, testUser.getPassword());
    }

    @Test
    void testToString() {
        String theString = testUser.toString();
        assertEquals(theString, "UserEntity{"
            + "username='" + username + '\''
            + ", password='" + password + '\''
            + '}');
    }

    @Test
    void testEquals_itself() {
        assertEquals(testUser, testUser);
        assertEquals(testUser.hashCode(), testUser.hashCode());
    }

    @Test
    void testEquals_otherClass() {
        Object o = new ArrayList<>();
        assertNotEquals(testUser, o);
        assertNotEquals(testUser.hashCode(), o.hashCode());
    }

    @Test
    void testEquals_differentPassword() {
        UserEntity newUser = new UserEntity(username, password2);
        assertEquals(newUser, testUser);
        assertEquals(newUser.hashCode(), testUser.hashCode());
    }

    @Test
    void testEquals_null() {
        assertNotEquals(testUser, null);
    }
}