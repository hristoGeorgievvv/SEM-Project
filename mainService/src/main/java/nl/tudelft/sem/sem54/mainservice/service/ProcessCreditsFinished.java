package nl.tudelft.sem.sem54.mainservice.service;

import java.util.Map;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;
import nl.tudelft.sem.sem54.mainservice.service.base.ProcessCredits;
import org.springframework.stereotype.Service;

/**
 * Process the credits for when a product is finished.
 */
@Service
public class ProcessCreditsFinished extends ProcessCredits {
    private transient UserService userService;

    public ProcessCreditsFinished(UserService userService) {
        this.userService = userService;
    }

    /**
     * Process the credits for when a product is finished.
     * The owner gets the amount credits that the product is worth.
     * Everyone who used it will lose credits for every portion that they used
     * There are no remaining credits and portions.
     *
     * @param product      the product to divide the credits from
     * @param userPortions the map with the users and how much portions they each took
     * @throws PortionsDoNotFitException the exception thrown when the amount of portions used
     *                                   is larger then the amount of portions in a product
     */
    @Override
    public void process(ProductEntity product, Map<UserEntity, Integer> userPortions)
        throws PortionsDoNotFitException {

        super.process(product, userPortions);

        userService.saveAll(userPortions.keySet());
        userService.save(product.getOwner());
    }

    /**
     * Throw PortionsDoNotFitException when the amount of portions used,
     * is not the same amount of portions in the product.
     *
     * @param product      the product to compare the portions with
     * @param userPortions the map with the users and how much portions they each took
     * @throws PortionsDoNotFitException the exception thrown
     */
    protected void fits(ProductEntity product, Map<UserEntity, Integer> userPortions)
        throws PortionsDoNotFitException {
        int usedPortions = userPortions.values().stream().reduce(0, Integer::sum);

        if (usedPortions > product.getPortions()) {
            throw new PortionsDoNotFitException("More portions used then available");
        }

        if (usedPortions < product.getPortions()) {
            throw new PortionsDoNotFitException("Less portions used then available");
        }
    }
}
