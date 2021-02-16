package nl.tudelft.sem.sem54.fridge.service.base;

import nl.tudelft.sem.sem54.fridge.domain.User;
import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    List<User> saveAll(List<User> list);

    void deleteAll();

    void deleteById(Long id);

    User findByUsername(String username);

    User findByUsernameOrCreateNewUser(String name);

    void checkUsernamesList(List<String> usernames);
}
