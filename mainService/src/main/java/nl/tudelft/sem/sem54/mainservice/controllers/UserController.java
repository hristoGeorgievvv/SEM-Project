package nl.tudelft.sem.sem54.mainservice.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.http.HttpResponse;
import java.util.Optional;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.service.HttpCommunicationService;
import nl.tudelft.sem.sem54.mainservice.service.LoggedInUserService;
import nl.tudelft.sem.sem54.mainservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * The mainMicroservice user controller.
 */
@RestController
@PropertySource("classpath:application.properties")
@RequestMapping("main")
public class UserController {

    private final transient UserService userService;
    private final transient HttpCommunicationService httpCommunicationService;
    private final transient LoggedInUserService loggedInUserService;

    @Value("${project.auth.port}")
    private transient String port;

    /**
     * The controller that controller the user information from the endpoints.
     *
     * @param userService              the user service to use
     * @param httpCommunicationService the http communication service to use
     * @param loggedInUserService      the logged in user service to use
     */
    public UserController(UserService userService,
                          HttpCommunicationService httpCommunicationService,
                          LoggedInUserService loggedInUserService) {
        this.userService = userService;
        this.httpCommunicationService = httpCommunicationService;
        this.loggedInUserService = loggedInUserService;
    }

    /**
     * Login to the application. This request will reach the auth microservice.
     *
     * @param jsonBody The body of the request, being user login details
     * @return The JWT received form the auth microservice
     */
    @PostMapping("login")
    @ResponseBody
    public String loginUser(@RequestBody String jsonBody) {
        HttpResponse<String> response = httpCommunicationService
                .postUtility("auth/login", port, null, jsonBody);
        if (response.statusCode() != HttpStatus.OK.value()) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(response.statusCode()), "Invalid Credentials");
        }
        //Add the user to the database if the user does not already exist
        userService.addUserIfNotInDatabase(getUsername(jsonBody));
        return response.body();
    }

    //Helper method to extract the username from the supplied json body
    private String getUsername(String jsonBody) {
        //GSON library to change string into a JSON object
        JsonObject userDetails = new Gson().fromJson(jsonBody, JsonObject.class);
        //Return username field from the object
        return userDetails.get("username").getAsString();
    }

    /**
     * Gets an iterable list of all flagged users.
     *
     * @return Iterable of userEntity with (<50) credits
     */
    @GetMapping("flagged")
    @ResponseBody
    public Iterable<UserEntity> getFlaggedUsers() {
        return userService.findFlaggedUsers();
    }

    /**
     * Resets credit count for all user entities in the database.
     */
    @PostMapping("reset")
    @ResponseStatus(value = HttpStatus.OK)
    public void resetUserCredits() {
        userService.setAllCreditsToZero();
    }

    /**
     * Gets all users with their credits from the database.
     *
     * @return Iterable of all users in the database
     */
    @GetMapping("users")
    @ResponseBody
    public Iterable<UserEntity> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Get the credit status of the currently logged in user.
     *
     * @return the amount of credits the logged in user has.
     */
    @GetMapping("credits/me")
    @ResponseBody
    public float myUsername() {
        Optional<UserEntity> user = loggedInUserService.getUser();
        if (user.isPresent()) {
            return user.get().getCredits();

        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    /**
     * Get the flagged status of the currently logged in user.
     *
     * @return the flagged status of the logged in user.
     */
    @GetMapping("flagged/me")
    @ResponseBody
    public boolean myFlagged() {
        Optional<UserEntity> user = loggedInUserService.getUser();
        if (user.isPresent()) {
            return user.get().getFlagged();

        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    /**
     * Deletes the user using the JWT token given.
     *
     * @param token the JWT token of the user
     */
    @DeleteMapping("user/me")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@RequestHeader(name = "Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        HttpResponse<String> response = httpCommunicationService
                .deleteUtility("auth/user/me", port, token);
        if (response.statusCode() != HttpStatus.OK.value()) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(response.statusCode()), "Invalid JWT Token");
        }

        Optional<UserEntity> user = loggedInUserService.getUser();
        if (user.isPresent()) {
            userService.deleteUser(user.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User not found");
        }

    }
}