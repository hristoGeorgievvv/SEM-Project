package nl.tudelft.sem.sem54.mainservice.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;
import nl.tudelft.sem.sem54.mainservice.redis.schema.MessageType;
import nl.tudelft.sem.sem54.mainservice.redis.schema.ProductStatus;
import nl.tudelft.sem.sem54.mainservice.service.ProcessCreditsFinished;
import nl.tudelft.sem.sem54.mainservice.service.ProcessCreditsRemoved;
import nl.tudelft.sem.sem54.mainservice.service.ProcessCreditsSpoiled;
import nl.tudelft.sem.sem54.mainservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
class RedisAdapterTest {

    @MockBean
    ProcessCreditsFinished processCreditsFinished;
    @MockBean
    ProcessCreditsSpoiled processCreditsSpoiled;
    @MockBean
    ProcessCreditsRemoved processCreditsRemoved;
    @MockBean
    UserService userService;

    @Autowired
    RedisAdapter redisAdapter;

    ProductStatus productStatus;

    UserEntity user1;
    UserEntity user2;
    UserEntity user3;
    private String partOfJson;

    @BeforeEach
    void beforeEach() {
        user1 = new UserEntity("testerdetest");
        user2 = new UserEntity("mark");
        user3 = new UserEntity("bobby");

        Map<String, Integer> map = new HashMap<>();
        map.put(user2.getUsername(), 3);
        map.put(user3.getUsername(), 9);
        productStatus = new ProductStatus(MessageType.PRODUCT_FINISHED,
            user1.getUsername(), 12, 23, map);
        partOfJson = "\"owner_username\":\"" + user1.getUsername() + "\","
            + "\"total_portions\":12,"
            + "\"total_credit_value\":23,"
            + "\"portions_per_user\":"
            + "{"
            + "\"" + user2.getUsername() + "\":3,"
            + "\"" + user3.getUsername() + "\":9"
            + "}"
            + "}";
    }

    @Test
    void testJsonToProductStatusFinished_productFinished() {
        productStatus.setMessageType(MessageType.PRODUCT_FINISHED);

        ProductStatus result = redisAdapter.jsonToProductStatus("{"
            + "\"message_type\":\"PRODUCT_FINISHED\","
            + partOfJson);

        assertThat(result).isEqualTo(productStatus);
    }

    @Test
    void testJsonToProductStatusFinished_productSpoiled() {
        productStatus.setMessageType(MessageType.PRODUCT_SPOILED);

        ProductStatus result = redisAdapter.jsonToProductStatus("{"
            + "\"message_type\":\"PRODUCT_SPOILED\","
            + partOfJson);

        assertThat(result).isEqualTo(productStatus);
    }

    @Test
    void testJsonToProductStatusFinished_productRemoved() {
        productStatus.setMessageType(MessageType.PRODUCT_REMOVED);

        ProductStatus result = redisAdapter.jsonToProductStatus("{"
            + "\"message_type\":\"PRODUCT_REMOVED\","
            + partOfJson);

        assertThat(result).isEqualTo(productStatus);
    }

    @Test
    void testMakeProduct_success() {
        when(userService.findByUsername(eq(user1.getUsername())))
            .thenReturn(Optional.of(user1));

        ProductEntity expected = new ProductEntity(12, 23, user1);
        ProductEntity result = redisAdapter.makeProduct(productStatus);

        assertThat(result.getOwner()).isSameAs(expected.getOwner());
        assertThat(result.getPortions()).isEqualTo(expected.getPortions());
        assertThat(result.getCredits()).isEqualTo(expected.getCredits());

        verify(userService, times(1))
            .findByUsername(eq(user1.getUsername()));
    }

    @Test
    void testMakeProduct_noOwner() {
        when(userService.findByUsername(eq(user1.getUsername())))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> redisAdapter.makeProduct(productStatus))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Product owner \"" + user1.getUsername() + "\" not found");

        verify(userService, times(1))
            .findByUsername(eq(user1.getUsername()));
    }

    @Test
    void testMakeUserPortionsMap_success() {
        when(userService.findByUsername(eq(user2.getUsername())))
            .thenReturn(Optional.of(user2));
        when(userService.findByUsername(eq(user3.getUsername())))
            .thenReturn(Optional.of(user3));

        Map<UserEntity, Integer> expected = new HashMap<>();
        expected.put(user2, 3);
        expected.put(user3, 9);

        Map<UserEntity, Integer> result = redisAdapter.makeUserPortionsMap(productStatus);

        assertThat(result).isEqualTo(expected);

        verify(userService, times(1))
            .findByUsername(eq(user3.getUsername()));
    }

    @Test
    void testMakeUserPortionsMap_noOwner() {
        when(userService.findByUsername(eq(user2.getUsername())))
            .thenReturn(Optional.of(user2));
        when(userService.findByUsername(eq(user3.getUsername())))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> redisAdapter.makeUserPortionsMap(productStatus))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Product owner \"" + user3.getUsername() + "\" not found");

        verify(userService, times(1))
            .findByUsername(eq(user3.getUsername()));
    }

    @Test
    void testProcess_success() throws PortionsDoNotFitException {
        when(userService.findByUsername(eq(user1.getUsername())))
            .thenReturn(Optional.of(user1));
        when(userService.findByUsername(eq(user2.getUsername())))
            .thenReturn(Optional.of(user2));
        when(userService.findByUsername(eq(user3.getUsername())))
            .thenReturn(Optional.of(user3));

        redisAdapter.process("{"
            + "\"message_type\":\"PRODUCT_FINISHED\","
            + partOfJson);


        ProductEntity expectedProduct = new ProductEntity(12, 23, user1);

        Map<UserEntity, Integer> expectedMap = new HashMap<>();
        expectedMap.put(user2, 3);
        expectedMap.put(user3, 9);

        ArgumentCaptor<Map<UserEntity, Integer>> captor
            = ArgumentCaptor.forClass(Map.class);
        verify(processCreditsFinished, times(1))
            .process(eq(expectedProduct), captor.capture());
        Map<UserEntity, Integer> capturedMap = captor.getAllValues().get(0);
        assertThat(capturedMap).containsExactlyInAnyOrderEntriesOf(expectedMap);
    }

    @Test
    void testpProcess_throwsProductFinishedException() throws PortionsDoNotFitException {
        when(userService.findByUsername(eq(user1.getUsername())))
            .thenReturn(Optional.of(user1));
        when(userService.findByUsername(eq(user2.getUsername())))
            .thenReturn(Optional.of(user2));
        when(userService.findByUsername(eq(user3.getUsername())))
            .thenReturn(Optional.of(user3));
        doThrow(PortionsDoNotFitException.class).when(processCreditsFinished)
            .process(any(), any());

        redisAdapter.process("{"
            + "\"message_type\":\"PRODUCT_FINISHED\","
            + partOfJson);


        ProductEntity expectedProduct = new ProductEntity(12, 23, user1);

        Map<UserEntity, Integer> expectedMap = new HashMap<>();
        expectedMap.put(user2, 3);
        expectedMap.put(user3, 9);

        ArgumentCaptor<Map<UserEntity, Integer>> captor
            = ArgumentCaptor.forClass(Map.class);
        verify(processCreditsFinished, times(1))
            .process(eq(expectedProduct), captor.capture());
        Map<UserEntity, Integer> capturedMap = captor.getAllValues().get(0);
        assertThat(capturedMap).containsExactlyInAnyOrderEntriesOf(expectedMap);
    }
}