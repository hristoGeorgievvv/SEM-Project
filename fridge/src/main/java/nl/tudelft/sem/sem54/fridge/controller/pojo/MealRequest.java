package nl.tudelft.sem.sem54.fridge.controller.pojo;

import java.util.HashMap;
import java.util.List;

public class MealRequest {

    private List<String> usernames;
    private HashMap<Long, Integer> products;

    /**
     * Create a MealRequest.
     *
     * @param usernames The list of usernames of participating users
     * @param products The map of products with the amount of portions used per user
     */
    public MealRequest(List<String> usernames, HashMap<Long, Integer> products) {
        this.usernames = usernames;
        this.products = products;
    }

    /**
     * Check if a MealRequest is valid, e.g. if it includes more than one user
     * and some products.
     *
     * @return True if the meal request has at least two participants and one product
     */
    public boolean isValid() {
        return (this.getUsernames() != null && this.getUsernames().size() > 1)
                && (this.getProducts() != null && this.getProducts().size() >= 1);
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public HashMap<Long, Integer> getProducts() {
        return products;
    }
}
