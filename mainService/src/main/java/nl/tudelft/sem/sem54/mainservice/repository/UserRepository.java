package nl.tudelft.sem.sem54.mainservice.repository;

import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Iterable<UserEntity> getUserEntitiesByCreditsLessThan(float credits);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserEntity u set u.credits = :credits")
    void updateAllCredits(@Param(value = "credits") float credits);

}
