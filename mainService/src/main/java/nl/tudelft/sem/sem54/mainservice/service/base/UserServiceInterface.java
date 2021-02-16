package nl.tudelft.sem.sem54.mainservice.service.base;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;

public interface UserServiceInterface {
    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    UserEntity save(UserEntity userEntity);

    Collection<UserEntity> saveAll(Collection<UserEntity> list);

    void deleteAll();

    Iterable<UserEntity> findFlaggedUsers();

    void deleteById(Long id);

    Optional<UserEntity> findByUsername(String username);

    void setAllCreditsToZero();

    void addUserIfNotInDatabase(String username);

    public void deleteUser(UserEntity userEntity);
}
