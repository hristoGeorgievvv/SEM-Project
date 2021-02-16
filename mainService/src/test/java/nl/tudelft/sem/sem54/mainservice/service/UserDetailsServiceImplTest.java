package nl.tudelft.sem.sem54.mainservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserService userService;

    private String username;

    @BeforeEach
    public void beforeEach() {
        username = "bob";
    }

    @Test
    public void testLoadUserByUsername_userNotFound() {
        when(userService.findByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    public void testLoadUserByUsername_success() {
        when(userService.findByUsername(username))
                .thenReturn(Optional.of(new UserEntity(username)));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(userDetails.getUsername(), username);
    }
}
