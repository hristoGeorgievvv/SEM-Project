package nl.tudelft.sem.sem54.fridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void testLoadUserByUsername_success() {
        String username = "admin";
        User user = new User(username, new Fridge());
        Optional<User> optionalUserEntity = Optional.of(user);
        when(userRepository.findByUsername(username)).thenReturn(optionalUserEntity);

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

        assertEquals(userDetailsService.loadUserByUsername(username).getUsername(), username);
    }

    @Test
    void testLoadUserByUsername_userDoesNotExist() {
        when(userRepository.findByUsername("notadmin")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> new UserDetailsServiceImpl(userRepository).loadUserByUsername("notadmin"));
    }
}