package nl.tudelft.sem.sem54.mainservice.service;

import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserService {

    UserService userService;

    public LoggedInUserService(UserService userService) {
        this.userService = userService;
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
            return userService.findByUsername(authentication.getName());
        }
        return Optional.empty();
    }
}
