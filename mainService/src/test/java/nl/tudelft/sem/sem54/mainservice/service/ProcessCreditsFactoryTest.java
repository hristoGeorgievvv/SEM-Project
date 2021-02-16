package nl.tudelft.sem.sem54.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.tudelft.sem.sem54.mainservice.redis.schema.MessageType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProcessCreditsFactoryTest {
    @MockBean
    ProcessCreditsFinished processCreditsFinished;
    @MockBean
    ProcessCreditsSpoiled processCreditsSpoiled;
    @MockBean
    ProcessCreditsRemoved processCreditsRemoved;

    @Autowired
    ProcessCreditsFactory processCreditsFactory;

    @Test
    void getProcessor_productFinished() {
        assertThat(processCreditsFactory.getInstance(MessageType.PRODUCT_FINISHED))
            .isEqualTo(processCreditsFinished);
    }

    @Test
    void getProcessor_productSpoiled() {
        assertThat(processCreditsFactory.getInstance(MessageType.PRODUCT_SPOILED))
            .isEqualTo(processCreditsSpoiled);
    }

    @Test
    void getProcessor_productRemoved() {
        assertThat(processCreditsFactory.getInstance(MessageType.PRODUCT_REMOVED))
            .isEqualTo(processCreditsRemoved);
    }

    @Test
    void getProcessor_productDoesNotExist() {
        assertThatThrownBy(() -> processCreditsFactory.getInstance(null))
            .isInstanceOf(RuntimeException.class);
    }
}