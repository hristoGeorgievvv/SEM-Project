package nl.tudelft.sem.sem54.authorizarionsevice.services;

import java.util.Optional;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import nl.tudelft.sem.sem54.authorizarionsevice.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserService {

    private final UserRepository userRepository;

    public LoggedInUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get the user entity of the current user if this user exists in
     * the main service databse.
     *
     * @return optional of the user. empty if user not found.
     */
    public Optional<UserEntity> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userRepository.findByUsername(authentication.getName());
        }
        return Optional.empty();
    }
}
