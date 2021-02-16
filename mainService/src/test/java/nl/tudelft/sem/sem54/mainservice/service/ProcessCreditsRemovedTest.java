package nl.tudelft.sem.sem54.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProcessCreditsRemovedTest {

    private ProductEntity product1;

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;
    private final Map<UserEntity, Integer> map = new HashMap<>();

    @Autowired
    private ProcessCreditsRemoved processCreditsRemoved;

    @MockBean
    private transient UserService userService;

    private final Offset<Float> floatOffset = Offset.offset((float) 1.0e-5);

    @BeforeEach
    void beforeEach() {
        user1 = new UserEntity("user1", 0);
        user2 = new UserEntity("user2", 0);
        user3 = new UserEntity("user3", 0);
        product1 = new ProductEntity(5, 100, user1);

        List<UserEntity> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);

        when(userService.findAll()).thenReturn(allUsers);
    }

    @Test
    void testProcessCreditsRemoved_userTakesMorePortions() {
        map.put(user2, 20);
        map.put(user1, 3);
        assertThrows(PortionsDoNotFitException.class,
                () -> processCreditsRemoved.process(product1, map));
    }

    @Test
    void testProcessCreditsRemoved_verifySaveWhenAllUsersTook() throws PortionsDoNotFitException {
        when(userService.save(eq(user1))).thenAnswer(i -> i.getArguments()[0]);
        when(userService.saveAll(any(Collection.class))).thenAnswer(i -> i.getArguments()[0]);

        map.put(user2, 2);
        map.put(user3, 3);
        processCreditsRemoved.process(product1, map);

        verify(userService, times(1)).save(user1);

        ArgumentCaptor<Collection<UserEntity>> usersCaptor
            = ArgumentCaptor.forClass(Collection.class);
        verify(userService, times(1)).saveAll(usersCaptor.capture());
        Collection<UserEntity> capturedUsers = usersCaptor.getAllValues().get(0);
        assertThat(capturedUsers).containsExactlyInAnyOrder(user2, user3);
    }

    @Test
    void testProcessCreditsRemoved_verifySaveWhenSomeUsersTook() throws PortionsDoNotFitException {
        when(userService.save(eq(user1))).thenAnswer(i -> i.getArguments()[0]);
        when(userService.saveAll(any(Collection.class))).thenAnswer(i -> i.getArguments()[0]);

        map.put(user2, 2);
        processCreditsRemoved.process(product1, map);

        verify(userService, times(1)).save(user1);

        ArgumentCaptor<Collection<UserEntity>> usersCaptor
            = ArgumentCaptor.forClass(Collection.class);
        verify(userService, times(1)).saveAll(usersCaptor.capture());
        Collection<UserEntity> capturedUsers = usersCaptor.getAllValues().get(0);
        assertThat(capturedUsers).containsExactlyInAnyOrder(user2);
    }

    @Test
    void testProcessCreditsRemoved_allUsersTook() throws PortionsDoNotFitException {
        map.put(user1, 1);
        map.put(user2, 1);
        map.put(user3, 1);

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 20.0 - 40.0 / 3.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-20.0 - 40.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-20.0 - 40.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsRemoved_allUsersTookAndHadNonZeroCreditBalance() throws PortionsDoNotFitException {
        map.put(user1, 1);
        map.put(user2, 1);
        map.put(user3, 1);

        user1.setCredits(123);
        user2.setCredits(234);
        user3.setCredits(-123);

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (123.0 + 100.0 - 20.0 - 40.0 / 3.0),
            floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (234.0 - 20.0 - 40.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-123.0 - 20.0 - 40.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsRemoved_someUsersTook() throws PortionsDoNotFitException {
        map.put(user2, 2);
        map.put(user3, 2);

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-40.0 - 20.0 * 2.0 / 4.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-40.0 - 20.0 * 2.0 / 4.0), floatOffset);
    }

    @Test
    void testProcessCreditsRemoved_productFinished() throws PortionsDoNotFitException {
        map.put(user2, 2);
        map.put(user3, 3);

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-40.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-60.0), floatOffset);
    }

    @Test
    void testProcessCreditsRemoved_onlyOneUserTook() throws PortionsDoNotFitException {
        map.put(user2, 1);

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-100.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (0), floatOffset);
    }

    @Test
    void testProcessCreditsRemoved_onlyOwnerTook() throws PortionsDoNotFitException {
        map.put(user1, 4);

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 100.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (0), floatOffset);
    }

    @Test
    void testProcessCreditsRemoved_nobodyTook() throws PortionsDoNotFitException {

        processCreditsRemoved.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (0), floatOffset);
    }
}