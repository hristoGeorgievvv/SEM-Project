package nl.tudelft.sem.sem54.mainservice.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.repository.UserRepository;
import nl.tudelft.sem.sem54.mainservice.service.base.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {
    UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Collection<UserEntity> saveAll(Collection<UserEntity> userEntities) {
        return userRepository.saveAll(userEntities);
    }

    @Override
    public Iterable<UserEntity> findFlaggedUsers() {
        return userRepository.getUserEntitiesByCreditsLessThan(-50.0f);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void setAllCreditsToZero() {
        userRepository.updateAllCredits(0);
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    /**
     * We add a user to the database if it does not exist yet, setting
     * the amount of credits to 0.
     *
     * @param username The username of the user to add.
     */
    @Override
    public void addUserIfNotInDatabase(String username) {
        Optional<UserEntity> ue = findByUsername(username);
        if (ue.isEmpty()) {
            userRepository.save(new UserEntity(username, 0.0f));
        }
    }
}

