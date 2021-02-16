package nl.tudelft.sem.sem54.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProcessCreditsFinishedTest {

    private ProductEntity product1;

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;

    @Autowired
    private ProcessCreditsFinished processCreditsFinished;

    @MockBean
    private transient UserService userService;

    @BeforeEach
    void beforeEach() {
        user1 = new UserEntity("user1", 0);
        user2 = new UserEntity("user2", 10);
        user3 = new UserEntity("user3", -10);
        product1 = new ProductEntity(5, 100, user1);
    }

    @Test
    void testProcessCreditsFinished_usedLessPortions() {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 2);
        map.put(user1, 2);
        assertThrows(PortionsDoNotFitException.class, () -> {
            processCreditsFinished.process(product1, map);
        });
    }

    @Test
    void testProcessCreditsFinished_usedMorePortions() {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 20);
        map.put(user1, 3);
        assertThrows(PortionsDoNotFitException.class, () -> {
            processCreditsFinished.process(product1, map);
        });
    }

    @Test
    void testProcessCreditsFinished_successVerifyUsersAreSaved() throws PortionsDoNotFitException {
        when(userService.save(eq(user1))).thenAnswer(i -> i.getArguments()[0]);
        when(userService.saveAll(any(Collection.class))).thenAnswer(i -> i.getArguments()[0]);

        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 2);
        map.put(user3, 3);
        processCreditsFinished.process(product1, map);

        verify(userService, times(1)).save(user1);

        ArgumentCaptor<Collection<UserEntity>> usersCaptor
            = ArgumentCaptor.forClass(Collection.class);
        verify(userService, times(1)).saveAll(usersCaptor.capture());
        Collection<UserEntity> capturedUsers = usersCaptor.getAllValues().get(0);
        assertThat(capturedUsers).containsExactlyInAnyOrder(user2, user3);
    }

    @Test
    void testProcessCreditsFinished_divideAmongAllUsers() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user1, 1);
        map.put(user2, 1);
        map.put(user3, 3);
        processCreditsFinished.process(product1, map);
        assertThat(user1.getCredits()).isEqualTo(80);
        assertThat(user2.getCredits()).isEqualTo(-10);
        assertThat(user3.getCredits()).isEqualTo(-70);
    }

    @Test
    void testProcessCreditsFinished_divideAmongSomeUsers() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 2);
        map.put(user3, 3);
        processCreditsFinished.process(product1, map);
        assertThat(user1.getCredits()).isEqualTo(100);
        assertThat(user2.getCredits()).isEqualTo(-30);
        assertThat(user3.getCredits()).isEqualTo(-70);
    }

    @Test
    void testProcessCreditsFinished_divideAmongOneUser() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 5);
        processCreditsFinished.process(product1, map);
        assertThat(user1.getCredits()).isEqualTo(100);
        assertThat(user2.getCredits()).isEqualTo(-90);
    }

    @Test
    void testProcessCreditsFinished_onlyOneOwner() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user1, 5);
        processCreditsFinished.process(product1, map);
        assertThat(user1.getCredits()).isEqualTo(0);
    }
}