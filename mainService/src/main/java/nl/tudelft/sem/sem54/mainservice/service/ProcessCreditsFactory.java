package nl.tudelft.sem.sem54.mainservice.service;

import nl.tudelft.sem.sem54.mainservice.redis.schema.MessageType;
import nl.tudelft.sem.sem54.mainservice.service.base.ProcessCredits;
import org.springframework.stereotype.Service;

@Service
public class ProcessCreditsFactory {

    private final ProcessCreditsFinished processCreditsFinished;
    private final ProcessCreditsSpoiled processCreditsSpoiled;
    private final ProcessCreditsRemoved processCreditsRemoved;

    /**
     * Factory to decide what processor to use for which message type.
     *
     * @param processCreditsFinished The processor to use for when a product is finished
     * @param processCreditsSpoiled  The processor to use for when a product is spoiled
     * @param processCreditsRemoved  The processor to use for when a product is removed
     */
    public ProcessCreditsFactory(ProcessCreditsFinished processCreditsFinished,
                                 ProcessCreditsSpoiled processCreditsSpoiled,
                                 ProcessCreditsRemoved processCreditsRemoved) {
        this.processCreditsFinished = processCreditsFinished;
        this.processCreditsSpoiled = processCreditsSpoiled;
        this.processCreditsRemoved = processCreditsRemoved;
    }

    /**
     * Get the right processor for the right message type.
     *
     * @param messageType the productStatus object to get the message type from
     * @return an instance of ProcessCredits, based on the message type
     */
    public ProcessCredits getInstance(MessageType messageType) {
        switch (messageType) {
            case PRODUCT_FINISHED:
                return processCreditsFinished;
            case PRODUCT_SPOILED:
                return processCreditsSpoiled;
            case PRODUCT_REMOVED:
                return processCreditsRemoved;
            default:
                throw new RuntimeException("Bad message type");
        }
    }
}
