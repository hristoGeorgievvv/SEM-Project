package nl.tudelft.sem.sem54.fridge.init;

import java.time.LocalTime;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.ProductService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoader implements ApplicationRunner {


    private FridgeService fridgeService;
    private UserService userService;
    private ProductService productService;

    /**
     * Inject needed services.
     *
     * @param fridgeService  fridge service
     * @param userService    user service
     * @param productService product service
     */
    @Autowired
    public DataLoader(FridgeService fridgeService,
                      UserService userService, ProductService productService) {
        this.fridgeService = fridgeService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(LocalTime.now() + ": " + "Starting: DataLoader.");

        Fridge fridge = new Fridge();
        fridgeService.save(fridge);

        //        User user = new User("user");
        //        user.setFridge(fridge);
        //        userService.save(user);
        //
        //        Product product = new Product();
        //        product.setProductName("musaka");
        //        product.setFridge(fridge);
        //        product.setOwner(user);
        //        product.setPortionsLeft(5);
        //        product.setPortions(10);
        //        product.setCreditValue(100);
        //        product.setTransactionList(new ArrayList<>());
        //        product.setExpirationDate(Date.from(Instant.now()));
        //
        //        productService.save(product);
    }
}