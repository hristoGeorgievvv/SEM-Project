package nl.tudelft.sem.sem54.authorizarionsevice.services;

import java.util.ArrayList;
import java.util.Optional;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import nl.tudelft.sem.sem54.authorizarionsevice.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final transient UserRepository userRepository;

    /**
     * Custom implementation of the UserDetailsService.
     *
     * @param userRepository the repository containing user information
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method to get a User object given the username.
     *
     * @param username username of the user
     * @return org.springframework.security.core.userdetails.User object
     * @throws UsernameNotFoundException if username is not in repository
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> returnUser = userRepository.findByUsername(username);
        if (returnUser.isEmpty()) {
            throw new UsernameNotFoundException("User name \"" + username + "\" not found!");
        }
        return new User(returnUser.get().getUsername(), returnUser.get().getPassword(),
            new ArrayList<>()) {
        };
    }
}