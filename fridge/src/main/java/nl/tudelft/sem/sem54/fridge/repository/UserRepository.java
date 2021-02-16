package nl.tudelft.sem.sem54.fridge.repository;

import nl.tudelft.sem.sem54.fridge.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Override
    void delete(User user);

    @Override
    void deleteById(Long id);

    @Override
    void deleteAll();

    @Override
    Optional<User> findById(Long id);

}
