package nl.tudelft.sem.sem54.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void testFindById() {
        Optional<UserEntity> user = Optional.of(new UserEntity());
        when(userRepository.findById(eq((long) 3))).thenReturn(user);

        Optional<UserEntity> result = userService.findById((long) 3);

        assertThat(result).isEqualTo(user);
        verify(userRepository, times(1)).findById((long) 3);
    }

    @Test
    void testFindAll() {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity());
        when(userRepository.findAll()).thenReturn(userList);

        List<UserEntity> result = userService.findAll();

        assertThat(result).isEqualTo(userList);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        UserEntity user = new UserEntity();

        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveAll() {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity());

        userService.saveAll(userList);

        verify(userRepository, times(1)).saveAll(userList);
    }

    @Test
    void testFindFlaggedUsers() {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity());
        when(userRepository.getUserEntitiesByCreditsLessThan(-50.0f))
            .thenReturn(userList);

        Iterable<UserEntity> result = userService.findFlaggedUsers();

        assertThat(result).isEqualTo(userList);
        verify(userRepository, times(1))
            .getUserEntitiesByCreditsLessThan(-50.0f);
    }

    @Test
    void testDeleteAll() {
        userService.deleteAll();

        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteById() {
        userService.deleteById((long) 3);

        verify(userRepository, times(1)).deleteById((long) 3);
    }

    @Test
    void testFindByUsername() {
        String username = "piet";
        Optional<UserEntity> user = Optional.of(new UserEntity(username));
        when(userRepository.findByUsername(eq(username))).thenReturn(user);

        Optional<UserEntity> result = userService.findByUsername(username);

        assertThat(result).isEqualTo(user);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testSetAllCreditsToZero() {
        userService.setAllCreditsToZero();

        verify(userRepository, times(1)).updateAllCredits(0);
    }

    @Test
    void testAddUserIfNotInDatabase_success() {
        UserEntity testUser = new UserEntity("testUserToAdd", 0.0f);

        userService.addUserIfNotInDatabase("testUserToAdd");

        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testAddUserIfNotInDatabase_userAlreadyThere() {
        Optional<UserEntity> testUserOpt = Optional.of(new UserEntity("testUserNotToBeAdded", 0.0f));
        when(userRepository.findByUsername(eq("testUserNotToBeAdded"))).thenReturn(testUserOpt);

        userService.addUserIfNotInDatabase("testUserNotToBeAdded");
        verify(userRepository, times(0)).save(testUserOpt.get());
    }
}