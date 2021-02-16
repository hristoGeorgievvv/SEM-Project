package nl.tudelft.sem.sem54.fridge.jobs;

import java.util.Date;
import java.util.List;
import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.redis.ProductStatusPublisher;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ExpirationJob {

    private final ProductStatusPublisher productStatusPublisher;
    private final ProductService productService;

    /**
     * Autowire dependencies.
     *
     * @param productStatusPublisher product status publisher
     * @param productService         Product service
     */
    @Autowired
    public ExpirationJob(ProductStatusPublisher productStatusPublisher, ProductService productService) {
        this.productStatusPublisher = productStatusPublisher;
        this.productService = productService;
    }

    /**
     * Checks product expirations on a 24hr schedule.
     * If a product has expired it publishes an expiration event to Redis.
     * Does not mutate fridge.
     * Subscribers receiving the expiration event should decide how to handle it.
     * (Strategy to split product costs, etc.)
     */
    @Scheduled(cron = "0 0 */12 * * *")
    public void checkExpirationDates() {
        Date date = new Date();
        System.out.println(
                "Starting expiration date check - " + date.toString());
        List<Product> productList = productService.findAll();

        for (Product product : productList) {
            System.out.println("Checking product: " + product.getId());
            if (product.getExpirationDate().before(date)) {
                System.out.println(
                        "Found expired product: " + product.getId());

                productStatusPublisher.publishExpiration(product);
            }
        }

    }
}
