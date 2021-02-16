package nl.tudelft.sem.sem54.fridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoggedInUserServiceTest {
    @InjectMocks
    LoggedInUserService loggedInUserService;

    @Mock
    FridgeServiceImpl fridgeServiceMock;

    @Mock
    UserServiceImpl userServiceMock;

    @Mock
    SecurityContextHolder securityContextHolderMock;

    private Authentication authentication;
    private String username;

    @BeforeEach
    void beforeEach() {
        username = "admin";
        loggedInUserService = new LoggedInUserService(userServiceMock);
        authentication = new UsernamePasswordAuthenticationToken((Principal) () -> username, new Object());
    }

    @Test
    public void testGetUser_instanceOfAnonymousAuthenticationToken() {
        authentication = new AnonymousAuthenticationToken(
                "a", new Object(), Collections.singletonList(new SimpleGrantedAuthority(username)));

        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic
                     = Mockito.mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
                    .thenReturn(new SecurityContextImpl(authentication));

            assertThrows(UserNotFoundException.class, () -> loggedInUserService.getUser());
        }
    }

    @Test
    public void testGetUser_userIsPresent() {
        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic
                     = Mockito.mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
                    .thenReturn(new SecurityContextImpl(authentication));

            User user = new User(username);
            when(userServiceMock.findByUsernameOrCreateNewUser(username)).thenReturn(user);

            assertEquals(loggedInUserService.getUser(), user);
        }
    }
}
