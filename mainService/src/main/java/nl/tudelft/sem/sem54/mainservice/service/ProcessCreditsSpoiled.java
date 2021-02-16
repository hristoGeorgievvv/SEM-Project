package nl.tudelft.sem.sem54.mainservice.service;

import java.util.List;
import java.util.Map;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;
import nl.tudelft.sem.sem54.mainservice.service.base.ProcessCredits;
import org.springframework.stereotype.Service;

/**
 * Process the credits for when a product is spoiled.
 */
@Service
public class ProcessCreditsSpoiled extends ProcessCredits {
    private transient UserService userService;

    public ProcessCreditsSpoiled(UserService userService) {
        this.userService = userService;
    }

    /**
     * Process the credits for when a product is spoiled.
     * The owner gets the amount credits that the product is worth.
     * Everyone who used it will lose credits for every portion that they used
     * The remaining credits will be equally distributed over all the users.
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

        int usedPortions = userPortions.values().stream().reduce(0, Integer::sum);

        float leftOverCredits = (product.getPortions() - usedPortions)
            * product.getCreditsPerPortion();

        List<UserEntity> allUsers = userService.findAll();

        for (UserEntity user : allUsers) {
            user.addCredits(-leftOverCredits / allUsers.size());
        }

        userService.saveAll(allUsers);
    }


    /**
     * Throw PortionsDoNotFitException when the amount of portions used,
     * is more then the amount of portions in the product.
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
    }
}
