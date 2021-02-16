package nl.tudelft.sem.sem54.fridge.controller;

import nl.tudelft.sem.sem54.fridge.controller.pojo.MealRequest;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.exceptions.NotEnoughPortionsException;
import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import nl.tudelft.sem.sem54.fridge.exceptions.UserNotFoundException;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MealController {

    private final UserService userService;
    private final ProductService productService;
    private final FridgeController fridgeController;

    /**
     * Initialize dependencies.
     *
     * @param userService The user service
     * @param productService The product service
     */
    public MealController(UserService userService,
                          ProductService productService, FridgeController fridgeController) {
        this.userService = userService;
        this.productService = productService;
        this.fridgeController = fridgeController;
    }

    /**
     * Take a meal request and process it.
     *
     * @param mealRequest The meal request for a meal
     */
    @PostMapping("meal")
    public void processMeal(@RequestBody MealRequest mealRequest)
            throws ProductNotFoundException, UserNotFoundException, NotEnoughPortionsException {
        // Check for invalid input
        if (!mealRequest.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Please provide at least two users and at least one product");
        }

        // Check if all users from the mealRequest exist
        userService.checkUsernamesList(mealRequest.getUsernames());

        // Check if we have enough portions per product
        checkAvailablePortions(mealRequest.getProducts(), mealRequest.getUsernames().size());

        // Iterate over all products and update available portions
        for (Map.Entry<Long, Integer> product : mealRequest.getProducts().entrySet()) {
            long productId = product.getKey();
            int portions = product.getValue();

            // Takeout each product in the meal
            for (String username : mealRequest.getUsernames()) {
                fridgeController.takeProduct(username, productId, portions);
            }
        }
    }

    /**
     * Check the available portions for all products. Void when all portions are okay, throws an
     * exception if a product has too little portions.
     *
     * @param portionsRequired A HashMap with products as keys and integers as portions per user
     * @param numberOfUsers The amount of users participating in the meal
     */
    private void checkAvailablePortions(HashMap<Long, Integer> portionsRequired, int numberOfUsers)
            throws NotEnoughPortionsException {
        for (Map.Entry<Long, Integer> product : portionsRequired.entrySet()) {
            Product p = productService.findById(product.getKey());
            int portions = product.getValue();

            int neededPortions = portions * numberOfUsers;

            if (p.getPortionsLeft() < neededPortions) {
                throw new NotEnoughPortionsException(neededPortions,
                        p.getPortionsLeft(), p.getId());
            }
        }
    }
}