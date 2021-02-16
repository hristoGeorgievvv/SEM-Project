package nl.tudelft.sem.sem54.mainservice.entities;

import java.util.Objects;

/**
 * Product class, keeping track of portions and credits.
 */
public class ProductEntity {

    private int portions;
    private int credits;
    private UserEntity owner;

    /**
     * Constructor, taking the amount of portions and credits of a product.
     *
     * @param portions The amount of portions
     * @param credits  The amount of credits
     * @param owner    The owner of the food item
     */
    public ProductEntity(int portions, int credits, UserEntity owner) {
        this.portions = portions;
        this.credits = credits;
        this.owner = owner;
    }

    /**
     * Gets the amount of portions in a product.
     *
     * @return The amount of portions in a product
     */
    public int getPortions() {
        return portions;
    }

    /**
     * Gets the amount of credits a product is worth.
     *
     * @return The amount of credits the product is worth
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Set the amount of portions of the product.
     *
     * @param portions The new amount of portions
     */
    public void setPortions(int portions) {
        this.portions = portions;
    }

    /**
     * Set the amount of credits.
     *
     * @param credits The new amount of credits
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Gets the user entity that owns the product.
     *
     * @return The owner
     */
    public UserEntity getOwner() {
        return owner;
    }

    /**
     * Set the owner of the product.
     *
     * @param owner the owner
     */
    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    /**
     * Gets the amount of credits per portion of a product.
     *
     * @return The amount of credits per portion
     */
    public float getCreditsPerPortion() {
        return (float) this.getCredits() / (float) this.getPortions();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductEntity that = (ProductEntity) o;
        return portions == that.portions
            && credits == that.credits
            && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portions, credits, owner);
    }
}
