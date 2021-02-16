package nl.tudelft.sem.sem54.mainservice.service.base;

import java.util.Map;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;

/**
 * Process the credits for a product.
 */
public abstract class ProcessCredits {

    /**
     * Process the credits for a product.
     * The owner gets the amount credits that the product is worth.
     * Everyone who used it will lose credits for every portion that they used
     *
     * @param product      the product to divide the credits from
     * @param userPortions the map with the users and how much portions they each took
     * @throws PortionsDoNotFitException the exception thrown when the amount of portions used
     *                                   is larger then the amount of portions in a product
     */
    public void process(ProductEntity product, Map<UserEntity, Integer> userPortions)
        throws PortionsDoNotFitException {

        fits(product, userPortions);

        for (Map.Entry<UserEntity, Integer> entry : userPortions.entrySet()) {
            float diffCredits = entry.getValue()
                * (float) product.getCredits() / (float) product.getPortions();
            entry.getKey().addCredits(-diffCredits);
            if (entry.getKey().equals(product.getOwner())) {
                product.setOwner(entry.getKey());
            }
        }

        product.getOwner().addCredits((float) product.getCredits());
    }

    /**
     * Throw PortionsDoNotFitException when the amount of portions used, does not fit
     * in some way in the amount of portions in the product.
     *
     * @param product      the product to compare the portions with
     * @param userPortions the map with the users and how much portions they each took
     * @throws PortionsDoNotFitException the exception thrown
     */
    protected abstract void fits(ProductEntity product, Map<UserEntity, Integer> userPortions)
        throws PortionsDoNotFitException;
}
