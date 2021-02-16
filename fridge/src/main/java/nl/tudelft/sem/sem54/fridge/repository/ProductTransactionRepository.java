package nl.tudelft.sem.sem54.fridge.repository;

import java.util.Optional;
import nl.tudelft.sem.sem54.fridge.domain.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction, Long> {

    /**
     * Returns the last valid transaction of a user.
     *
     * @param productId the productId.
     * @param userId the userId.
     */
    default Optional<ProductTransaction> findLastValidTransaction(Long productId, Long userId) {
        return
                findTop1ByProductIdAndUserIdAndIsRevertFalseAndRevertedProductTransactionIsNullOrderByTimestampDesc(
                        productId, userId);
    }

    Optional<ProductTransaction>
            findTop1ByProductIdAndUserIdAndIsRevertFalseAndRevertedProductTransactionIsNullOrderByTimestampDesc(
                    long productId, long userId);


}
