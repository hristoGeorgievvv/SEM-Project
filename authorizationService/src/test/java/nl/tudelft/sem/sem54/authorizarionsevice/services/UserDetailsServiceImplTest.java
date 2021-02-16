package nl.tudelft.sem.sem54.authorizarionsevice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import nl.tudelft.sem.sem54.authorizarionsevice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
class UserDetailsServiceImplTest {

    @MockBean
    private transient UserRepository userRepository;

    private transient String username;
    private transient String username2;
    private transient String password;

    @BeforeEach
    void beforeEach() {
        username = UUID.randomUUID().toString();
        username2 = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString();
    }

    @Test
    void testLoadUserByUsername_success() {

        UserEntity user = new UserEntity(username, password);
        Optional<UserEntity> optionalUserEntity = Optional.of(user);
        when(userRepository.findByUsername(username)).thenReturn(optionalUserEntity);

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

        assertEquals(userDetailsService.loadUserByUsername(username).getUsername(), username);
    }

    @Test
    void testLoadUserByUsername_usernameNotFound() {
        Optional<UserEntity> optionalUserEntity = Optional.empty();
        when(userRepository.findByUsername(username2)).thenReturn(optionalUserEntity);
        assertThrows(UsernameNotFoundException.class,
                () -> new UserDetailsServiceImpl(userRepository).loadUserByUsername(username2));
    }
}