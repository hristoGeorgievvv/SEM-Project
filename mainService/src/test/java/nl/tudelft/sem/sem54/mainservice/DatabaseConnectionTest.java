package nl.tudelft.sem.sem54.mainservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Transactional
@ActiveProfiles("ci")
public class DatabaseConnectionTest {
    UserService userService;

    @Autowired
    public DatabaseConnectionTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testConnection() {
        UserEntity userEntity1 = new UserEntity("bob", 0);
        userService.save(userEntity1);
        Optional<UserEntity> user = userService.findByUsername("bob");
        assertTrue(user.isPresent());
        assertEquals(userEntity1, user.get());

    }
}
