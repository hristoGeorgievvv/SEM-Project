package nl.tudelft.sem.sem54.fridge.service;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.UserNotFoundException;
import nl.tudelft.sem.sem54.fridge.repository.UserRepository;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FridgeService fridgeService;

    @Autowired
    UserServiceImpl(UserRepository userRepository, FridgeService fridgeService) {
        this.userRepository = userRepository;
        this.fridgeService = fridgeService;
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
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
    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByUsernameOrCreateNewUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username, fridgeService.getDefaultFridge())));
    }

    @Override
    public void checkUsernamesList(List<String> usernames) throws UserNotFoundException {
        for (String username : usernames) {
            this.findByUsername(username);
        }
    }
}

