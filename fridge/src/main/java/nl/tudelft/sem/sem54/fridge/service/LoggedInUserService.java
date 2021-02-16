package nl.tudelft.sem.sem54.fridge.service;

import java.util.Optional;
import nl.tudelft.sem.sem54.fridge.domain.User;
import nl.tudelft.sem.sem54.fridge.exceptions.UserNotFoundException;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import nl.tudelft.sem.sem54.fridge.service.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserService {

    private final UserService userService;

    public LoggedInUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the user entity of the current user if this user exists in
     * the fridge service database or create a new user in the default household.
     *
     * @return user or throw exception if there is a problem with the jwt token.
     */

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userService.findByUsernameOrCreateNewUser(authentication.getName());
        }
        throw new UserNotFoundException();
    }
}
