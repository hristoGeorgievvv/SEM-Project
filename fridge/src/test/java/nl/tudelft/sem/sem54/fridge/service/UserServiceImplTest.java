package nl.tudelft.sem.sem54.fridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.UserNotFoundException;
import nl.tudelft.sem.sem54.fridge.repository.FridgeRepository;
import nl.tudelft.sem.sem54.fridge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    UserRepository userRepositoryMock;

    @Test
    public void testFindById_userNotFound() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.findById(1L));
    }

    @Test
    public void testFindById_success() {
        User user = new User("stamat");
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(userServiceImpl.findById(1L), user);
    }

    @Test
    public void testCheckUsernamesList_userNotFound() {
        User user1 = new User("koen");
        when(userRepositoryMock.findByUsername("koen")).thenReturn(Optional.of(user1));
        when(userRepositoryMock.findByUsername("kostas")).thenReturn(Optional.empty());
        List<String> usernames = new ArrayList<>();
        usernames.add("koen");
        usernames.add("kostas");
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.checkUsernamesList(usernames));
    }

}
