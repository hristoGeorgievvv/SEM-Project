package nl.tudelft.sem.sem54.authorizarionsevice.controllers;

import java.util.Optional;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import nl.tudelft.sem.sem54.authorizarionsevice.repositories.UserRepository;
import nl.tudelft.sem.sem54.authorizarionsevice.services.LoggedInUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * The controller for the User entity.
 **/
@RestController
@RequestMapping("auth/user")
public class UserController {

    private final transient UserRepository repository;

    private final transient BCryptPasswordEncoder bcryptPasswordEncoder;

    private final transient LoggedInUserService loggedInUserService;

    /**
     * The controller that has the endpoint for the user.
     *
     * @param repository            the user repo to use
     * @param bcryptPasswordEncoder the password encoder to use
     * @param loggedInUserService   the service to get the logged in user to use
     */
    public UserController(UserRepository repository, BCryptPasswordEncoder bcryptPasswordEncoder,
                          LoggedInUserService loggedInUserService) {
        this.loggedInUserService = loggedInUserService;
        this.repository = repository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    /**
     * Gets an iterable list of all users.
     *
     * @return Iterable object of generic type UserEntity
     */
    @GetMapping()
    @ResponseBody
    public Iterable<UserEntity> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Create a new user.
     *
     * @param newUser a user with username and password to create
     */
    @PostMapping()
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void createUser(@RequestBody UserEntity newUser) {
        if (newUser == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide a username and password");
        }
        if (newUser.getUsername() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide a username");
        }
        if (newUser.getPassword() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide a password");
        }
        if (newUser.getUsername().equals("")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide a non-empty username");
        }
        if (newUser.getPassword().equals("")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide a non-empty password");
        }
        Optional<UserEntity> existingUser = repository.findByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "User " + newUser.getUsername() + " already exists");
        }
        newUser.setPassword(bcryptPasswordEncoder.encode(newUser.getPassword()));
        repository.save(newUser);
    }

    /**
     * Delete the currently logged in user.
     */
    @DeleteMapping("me")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser() {
        Optional<UserEntity> user = loggedInUserService.getUser();
        if (user.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "User does not exist.");
        }
        repository.delete(user.get());
    }
}
