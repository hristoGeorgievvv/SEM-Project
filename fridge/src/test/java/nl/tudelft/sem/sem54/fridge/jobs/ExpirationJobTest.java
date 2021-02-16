package nl.tudelft.sem.sem54.fridge.jobs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.redis.ProductStatusPublisher;
import nl.tudelft.sem.sem54.fridge.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExpirationJobTest {

    @InjectMocks
    ExpirationJob expirationJob;

    @Mock
    ProductStatusPublisher productStatusPublisherMock;

    @Mock
    ProductServiceImpl productServiceMock;

    private Product productFresh;
    private Product productSpoiled;

    /**
     * Setup variables for the testing environment.
     */

    @BeforeEach
    public void beforeEach() {
        Fridge fridge = new Fridge();
        productSpoiled = new Product("taratorche", 10, 5, 20,
                new Date(0), fridge);
        productFresh = new Product("taratorche", 10, 5, 20,
                new Date(Long.MAX_VALUE), fridge);
        User user = new User("alice");

        productSpoiled.setOwner(user);
    }

    @Test
    public void contextLoads() {
        assertThat(expirationJob).isNotNull();
    }

    @Test
    public void testCheckExpirationDate_verifyMessageSentOnSpoiledProduct() {
        when(productServiceMock.findAll()).thenReturn(List.of(productSpoiled));

        expirationJob.checkExpirationDates();

        verify(productStatusPublisherMock).publishExpiration(argThat(x -> {
            assertThat(x).isNotNull();
            assertThat(x).isEqualTo(productSpoiled);
            return true;
        }));
    }

    @Test
    public void testCheckExpirationDate_verifyNoMessageSentOnFreshProduct() {
        when(productServiceMock.findAll()).thenReturn(List.of(productFresh));

        expirationJob.checkExpirationDates();

        verify(productStatusPublisherMock, never()).publishExpiration(productFresh);
    }

    @Test
    public void testExpirationCheck_MultipleProducts() {
        productFresh.setOwner(new User("freshOwner"));
        productSpoiled.setOwner(new User("spoiledOwner"));

        when(productServiceMock.findAll()).thenReturn(List.of(productFresh, productSpoiled));

        expirationJob.checkExpirationDates();

        verify(productStatusPublisherMock, times(1)).publishExpiration(argThat(x -> {
            assertThat(x).isNotNull();
            assertThat(x).isEqualTo(productSpoiled);
            return true;
        }));
    }
}