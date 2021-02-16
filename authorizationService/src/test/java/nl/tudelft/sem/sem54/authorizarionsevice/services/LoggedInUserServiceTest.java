package nl.tudelft.sem.sem54.authorizarionsevice.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import nl.tudelft.sem.sem54.authorizarionsevice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class LoggedInUserServiceTest {

    @MockBean
    private transient UserRepository userRepository;

    @Autowired
    private transient LoggedInUserService loggedInUserService;

    private final String username = "bob";

    @Test
    @WithAnonymousUser
    void testGetUser_userIsAnonymous() {
        assertThat(loggedInUserService.getUser()).isEmpty();
    }

    @Test
    @WithMockUser(username)
    void testGetUser_success() {
        UserEntity user = new UserEntity(username, "password");
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

        assertThat(loggedInUserService.getUser()).containsSame(user);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @WithMockUser(username)
    void testGetUser_userDoesNotExist() {
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.empty());

        assertThat(loggedInUserService.getUser()).isEmpty();
        verify(userRepository, times(1)).findByUsername(username);
    }
}