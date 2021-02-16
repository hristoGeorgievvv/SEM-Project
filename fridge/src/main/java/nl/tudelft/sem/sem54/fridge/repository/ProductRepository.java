package nl.tudelft.sem.sem54.fridge.repository;

import nl.tudelft.sem.sem54.fridge.domain.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);

    boolean existsByProductName(String productName);
}
