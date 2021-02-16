package nl.tudelft.sem.sem54.fridge.redis;

import com.google.gson.Gson;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.redis.schema.MessageType;
import nl.tudelft.sem.sem54.fridge.redis.schema.ProductStatus;
import nl.tudelft.sem.sem54.fridge.service.PortionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductStatusPublisher {

    private final PortionServiceImpl portionService;
    private final RedisMessagePublisher productPublisher;

    @Autowired
    public ProductStatusPublisher(
            PortionServiceImpl portionService,
            RedisMessagePublisher productPublisher) {
        this.portionService = portionService;
        this.productPublisher = productPublisher;
    }

    /**
     * Publishes a PRODUCT_SPOILED message on the redis channel.
     *
     * @param product that has expired
     */
    public void publishExpiration(Product product) {
        Gson gson = new Gson();
        ProductStatus productStatus =
                new ProductStatus.Builder()
                        .forProduct(product)
                        .withPortionsPerUser(portionService.getUserPortionMap(product))
                        .withMessageType(MessageType.PRODUCT_SPOILED)
                        .build();

        String productStatusString = gson.toJson(productStatus);

        productPublisher.publish(productStatusString);
    }

    /**
     * Publishes a PRODUCT_REMOVED message on the redis channel.
     *
     * @param product that was removed
     */
    public void publishRemoval(Product product) {
        Gson gson = new Gson();
        ProductStatus productStatus =
                new ProductStatus.Builder()
                        .forProduct(product)
                        .withPortionsPerUser(portionService.getUserPortionMap(product))
                        .withMessageType(MessageType.PRODUCT_REMOVED)
                        .build();
        String productStatusString = gson.toJson(productStatus);

        productPublisher.publish(productStatusString);

    }

    /**
     * Publishes a PRODUCT_FINISHED message on the redis channel.
     *
     * @param product that was depleted
     */
    public void publishDepletion(Product product) {

        ProductStatus productStatus =
                new ProductStatus.Builder()
                        .forProduct(product)
                        .withPortionsPerUser(portionService.getUserPortionMap(product))
                        .withMessageType(MessageType.PRODUCT_FINISHED)
                        .build();
        Gson gson = new Gson();
        String productStatusString = gson.toJson(productStatus);

        productPublisher.publish(productStatusString);

    }
}
