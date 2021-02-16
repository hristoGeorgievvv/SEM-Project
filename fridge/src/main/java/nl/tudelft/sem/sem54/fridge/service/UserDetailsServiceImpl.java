package nl.tudelft.sem.sem54.fridge.service;

import nl.tudelft.sem.sem54.fridge.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Custom implementation of the UserDetailsService.
     *
     * @param us the user service
     */
    public UserDetailsServiceImpl(UserRepository us) {
        this.userRepository = us;
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
        Optional<nl.tudelft.sem.sem54.fridge.domain.User> returnUser =
                userRepository.findByUsername(username);
        if (returnUser.isEmpty()) {
            throw new UsernameNotFoundException(
                    "User name \"" + username + "\" not found!");
        }
        return new org.springframework.security.core.userdetails.User(
                returnUser.get().getUsername(), "any",
                new ArrayList<>()) {
        };
    }
}