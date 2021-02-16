package nl.tudelft.sem.sem54.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
class ProcessCreditsSpoiledTest {

    private ProductEntity product1;

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;
    private final Map<UserEntity, Integer> map = new HashMap<>();

    @Autowired
    private ProcessCreditsSpoiled processCreditsSpoiled;

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
    void testProcessCreditsSpoiled_tookMorePortions() {
        map.put(user2, 20);
        map.put(user1, 3);
        assertThrows(PortionsDoNotFitException.class, () -> {
            processCreditsSpoiled.process(product1, map);
        });
    }

    @Test
    void testProcessCreditsSpoiled_verifyUsersSaved() throws PortionsDoNotFitException {
        when(userService.saveAll(any(Collection.class))).thenAnswer(i -> i.getArguments()[0]);

        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 2);
        map.put(user3, 3);
        processCreditsSpoiled.process(product1, map);

        verify(userService, times(1)).findAll();

        ArgumentCaptor<Collection<UserEntity>> usersCaptor
            = ArgumentCaptor.forClass(Collection.class);
        verify(userService, times(1)).saveAll(usersCaptor.capture());
        Collection<UserEntity> capturedUsers = usersCaptor.getAllValues().get(0);
        assertThat(capturedUsers).containsExactlyInAnyOrder(user1, user2, user3);
    }

    @Test
    void testProcessCreditsSpoiled_allUsersTook() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user1, 1);
        map.put(user2, 1);
        map.put(user3, 1);

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 20.0 - 40.0 / 3.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-20.0 - 40.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-20.0 - 40.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsSpoiled_allUsersTookAndHadNonzeroCreditBalance() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user1, 1);
        map.put(user2, 1);
        map.put(user3, 1);

        user1.setCredits(123);
        user2.setCredits(234);
        user3.setCredits(-123);

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (123.0 + 100.0 - 20.0 - 40.0 / 3.0),
            floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (234.0 - 20.0 - 40.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-123.0 - 20.0 - 40.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsSpoiled_someUsersTook() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 2);
        map.put(user3, 2);

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 20.0 / 3.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-40.0 - 20.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-40.0 - 20.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsSpoiled_productIsFinished() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 2);
        map.put(user3, 3);

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-40.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-60.0), floatOffset);
    }

    @Test
    void testProcessCreditsSpoiled_oneUserTook() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user2, 1);

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 80.0 / 3.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-20.0 - 80.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-80.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsSpoiled_onlyOwnerTook() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();
        map.put(user1, 4);

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 80.0 - 20.0 / 3.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-20.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-20.0 / 3.0), floatOffset);
    }

    @Test
    void testProcessCreditsSpoiled_noUsersTook() throws PortionsDoNotFitException {
        Map<UserEntity, Integer> map = new HashMap<>();

        processCreditsSpoiled.process(product1, map);

        assertThat(user1.getCredits()).isCloseTo((float) (100.0 - 100.0 / 3.0), floatOffset);
        assertThat(user2.getCredits()).isCloseTo((float) (-100.0 / 3.0), floatOffset);
        assertThat(user3.getCredits()).isCloseTo((float) (-100.0 / 3.0), floatOffset);
    }
}