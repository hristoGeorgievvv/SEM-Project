package nl.tudelft.sem.sem54.fridge.domain.utils;

import nl.tudelft.sem.sem54.fridge.domain.Product;
import nl.tudelft.sem.sem54.fridge.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DomainUtils {

    public static List<User> createUserList() {
        return Stream.generate(User::new).limit(5).collect(Collectors.toUnmodifiableList());
    }

    public static List<Product> createProductList() {
        return Stream.generate(Product::new).limit(5).collect(Collectors.toUnmodifiableList());
    }
}
