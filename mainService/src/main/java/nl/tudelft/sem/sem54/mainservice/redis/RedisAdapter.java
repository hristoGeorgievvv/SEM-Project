package nl.tudelft.sem.sem54.mainservice.redis;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.ProductEntity;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.exceptions.PortionsDoNotFitException;
import nl.tudelft.sem.sem54.mainservice.redis.schema.ProductStatus;
import nl.tudelft.sem.sem54.mainservice.service.ProcessCreditsFactory;
import nl.tudelft.sem.sem54.mainservice.service.UserService;
import nl.tudelft.sem.sem54.mainservice.service.base.ProcessCredits;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RedisAdapter implements Adapter {
    private final ProcessCreditsFactory processCreditsFactory;
    private final UserService userService;
    private final Gson gson;

    /**
     * Processes a redis message.
     *
     * @param processCreditsFactory the factory for getting the right implementation for
     *                              processing credits
     * @param userService           the user service to use
     * @param gson                  the gson instance to use
     */
    public RedisAdapter(ProcessCreditsFactory processCreditsFactory,
                        UserService userService,
                        Gson gson) {
        this.processCreditsFactory = processCreditsFactory;
        this.userService = userService;
        this.gson = gson;
    }

    /**
     * Read in the json string and map it to a ProductStatus object.
     *
     * @param json the input json
     * @return the mapped ProductStatus object
     */
    protected ProductStatus jsonToProductStatus(String json) {
        return gson.fromJson(json, ProductStatus.class);
    }


    /**
     * Make a productEntity of the productStatus.
     *
     * @param productStatus the productStatus to extract the productEntity out of
     * @return the resulting ProductEntity
     * @throws UsernameNotFoundException when the username of the owner of the product
     *                                   is not found as a real user.
     */
    protected ProductEntity makeProduct(ProductStatus productStatus)
        throws UsernameNotFoundException {

        Optional<UserEntity> user = userService.findByUsername(productStatus.getOwnerUsername());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(
                "Product owner \"" + productStatus.getOwnerUsername() + "\" not found");
        }

        return new ProductEntity(productStatus.getTotalPortions(),
            productStatus.getTotalCreditValue(), user.get());
    }

    /**
     * Transform the user portions map from a ProductStatus to a user portions
     * map with real user entities.
     *
     * @param productStatus the productStatus to get the map from
     * @return A Map with user entities and how many portions they took.
     * @throws UsernameNotFoundException when one of the usernames is not found as a real user.
     */
    protected Map<UserEntity, Integer> makeUserPortionsMap(ProductStatus productStatus)
        throws UsernameNotFoundException {

        Map<UserEntity, Integer> result = new HashMap<>();

        for (Map.Entry<String, Integer> entry : productStatus.getPortionsPerUser().entrySet()) {
            Optional<UserEntity> user = userService.findByUsername(entry.getKey());
            if (user.isEmpty()) {
                throw new UsernameNotFoundException(
                    "Product owner \"" + entry.getKey() + "\" not found");
            }
            result.put(user.get(), entry.getValue());
        }

        return result;
    }

    /**
     * Process a redis message in json, to fully update all the credits.
     *
     * @param json the imput json message
     */
    @Override
    public void process(String json) {
        ProductStatus productStatus = jsonToProductStatus(json);
        ProcessCredits processor = processCreditsFactory
            .getInstance(productStatus.getMessageType());
        ProductEntity productEntity = makeProduct(productStatus);
        Map<UserEntity, Integer> userPortions = makeUserPortionsMap(productStatus);

        try {
            processor.process(productEntity, userPortions);
        } catch (PortionsDoNotFitException e) {
            e.printStackTrace();
        }
    }
}
