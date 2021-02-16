package nl.tudelft.sem.sem54.fridge.service;

import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PortionServiceImpl {


    /**
     * Creates a map that maps a username to the net amount of portions the user has taken.
     *
     * @return a HashMap from username to the net integer amount of portions.
     */
    public Map<String, Integer> getUserPortionMap(Product product) {
        Map<String, Integer> userPortionMap = new HashMap<>();
        for (ProductTransaction t : product.getTransactionList()) {
            String username = t.getUser().getUsername();
            int transactionPortions = t.getPortions();

            int currentPortions = userPortionMap.getOrDefault(username, 0);
            userPortionMap.put(username, currentPortions + transactionPortions);
        }
        return userPortionMap;
    }

}
