package nl.tudelft.sem.sem54.mainservice.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import javax.net.ssl.SSLSession;
import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import nl.tudelft.sem.sem54.mainservice.service.HttpCommunicationService;
import nl.tudelft.sem.sem54.mainservice.service.LoggedInUserService;
import nl.tudelft.sem.sem54.mainservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private transient UserService userService;

    @MockBean
    private transient HttpCommunicationService httpCommunicationService;

    @MockBean
    private transient LoggedInUserService loggedInUserService;

    private transient String endpointFlagged;
    private transient String endpointUsers;
    private transient String endpointUser;
    private transient String endpointReset;
    private transient String endpointLogin;
    private transient String endpointFlaggedMe;
    private transient String endpointCreditsMe;
    private transient String endpointDeleteUser;
    private transient String testToken;
    private transient String username;
    private transient String password;
    private transient float credits;
    private final String port = "8081";

    ObjectMapper mapper;

    private String makeContent(String username, String password) {
        return "{\"username\":\""
                + username
                + "\",\"password\":\""
                + password
                + "\"}";
    }

    HttpResponse<String> testResponseOk = new HttpResponse<>() {
        @Override
        public int statusCode() {
            return 200;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return null;
        }

        @Override
        public String body() {
            return testToken;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return null;
        }

        @Override
        public HttpClient.Version version() {
            return null;
        }
    };

    HttpResponse<String> testResponseNotOk = new HttpResponse<>() {
        @Override
        public int statusCode() {
            return 403;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return null;
        }

        @Override
        public String body() {
            return "Incorrect";
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return null;
        }

        @Override
        public HttpClient.Version version() {
            return null;
        }
    };

    @BeforeEach
    void beforeEach() {
        credits = 42;
        username = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString();
        endpointFlagged = "/main/flagged";
        endpointUser = "/auth/user";
        endpointUsers = "/main/users";
        endpointReset = "/main/reset";
        endpointLogin = "/main/login";
        mapper = new ObjectMapper();
        endpointFlaggedMe = "/main/flagged/me";
        endpointCreditsMe = "/main/credits/me";
        endpointDeleteUser = "auth/user/me";
        testToken = "testToken";
    }

    @Test
    void testGetUsers_notAuthenticated() throws Exception {
        credits = 0.0f;

        UserEntity user = new UserEntity(username, credits);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);

        when(userService.findAll()).thenReturn(iterable);

        this.mockMvc.perform(get(endpointUsers)).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void testGetUsers_isAuthenticatedSuccess() throws Exception {
        credits = 10.0f;

        float credits1 = -55.0f;
        String username1 = UUID.randomUUID().toString();

        UserEntity user1 = new UserEntity(username, credits);
        UserEntity user2 = new UserEntity(username1, credits1);
        ArrayList<UserEntity> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.findAll()).thenReturn(users);

        this.mockMvc.perform(get(endpointUsers)).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(mapper.writeValueAsString(users))));
    }

    @Test
    @WithMockUser
    void testGetFlaggedUsers_success() throws Exception {
        credits = -50.1f;
        UserEntity user = new UserEntity(username, credits);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);

        when(userService.findFlaggedUsers()).thenReturn(iterable);

        this.mockMvc.perform(get(endpointFlagged)).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(mapper.writeValueAsString(iterable))));
    }

    @Test
    void testGetFlaggedUsers_notAuthenticated() throws Exception {
        credits = -45.0f;
        UserEntity user = new UserEntity(username, credits);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);

        when(userService.findFlaggedUsers()).thenReturn(iterable);

        this.mockMvc.perform(get(endpointFlagged)).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void testGetFlaggedUsers_noUserIsFlagged() throws Exception {
        credits = -45.0f;
        UserEntity user = new UserEntity(username, credits);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);

        when(userService.findFlaggedUsers()).thenReturn(iterable);

        this.mockMvc.perform(get(endpointFlagged)).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("")));
    }

    @Test
    @WithMockUser
    void testGetFlaggedUsers_atCreditsBoundary() throws Exception {
        credits = -50.0f;
        UserEntity user = new UserEntity(username, credits);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);

        when(userService.findFlaggedUsers()).thenReturn(iterable);

        this.mockMvc.perform(get(endpointFlagged)).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("")));
    }

    @Test
    @WithMockUser
    void testResetUsers_success() throws Exception {
        this.mockMvc.perform(post(endpointReset)).andExpect(status().isOk());
        verify(userService, times(1)).setAllCreditsToZero();
    }

    @Test
    void testResetUsers_notAuthenticated() throws Exception {
        credits = -45.0f;
        UserEntity user = new UserEntity(username, credits);
        UserEntity user2 = new UserEntity(username + "diff", credits / 2.0f);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);
        iterable.add(user2);

        when(userService.findAll()).thenReturn(iterable);

        this.mockMvc.perform(get(endpointReset)).andExpect(status().is4xxClientError());
    }

    @Test
    void testLoginUser_success() throws Exception {

        String requestBody = makeContent(username, password);

        when(httpCommunicationService.postUtility(anyString(), anyString(), eq(null), anyString()))
                .thenReturn(testResponseOk);

        this.mockMvc.perform(post(endpointLogin)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("testToken")));
        verify(userService, times(1)).addUserIfNotInDatabase(username);
    }

    @Test
    void testLoginUser_fails() throws Exception {

        String requestBody = makeContent(username, password);

        when(httpCommunicationService.postUtility(anyString(), anyString(), eq(null), anyString()))
                .thenReturn(testResponseNotOk);

        this.mockMvc.perform(post(endpointLogin)
                .content(requestBody))
                .andExpect(status().isForbidden());
        verify(userService, times(0)).addUserIfNotInDatabase(username);
    }

    @Test
    void testLoginUser_nullUsername() throws Exception {

        String requestBody = makeContent(null, password);

        when(httpCommunicationService.postUtility(anyString(), anyString(), eq(null), anyString()))
                .thenReturn(testResponseNotOk);

        this.mockMvc.perform(post(endpointLogin)
                .content(requestBody))
                .andExpect(status().isForbidden());
        verify(userService, times(0)).addUserIfNotInDatabase(username);
    }

    @Test
    @WithMockUser("bob")
    void testMyCredits_success() throws Exception {
        username = "bob";
        credits = -50.0f;
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));

        this.mockMvc.perform(get(endpointCreditsMe)).andExpect(status().isOk())
            .andExpect(content().string("-50.0"));

        verify(loggedInUserService, times(1)).getUser();
    }

    @Test
    @WithMockUser
    void testMyCredits_userDoesNotExist() throws Exception {
        when(loggedInUserService.getUser()).thenReturn(Optional.empty());

        this.mockMvc.perform(get(endpointCreditsMe))
            .andExpect(status().isBadRequest());

        verify(loggedInUserService, times(1)).getUser();
    }

    @Test
    @WithAnonymousUser
    void testMyCredits_userIsAnonymous() throws Exception {
        when(loggedInUserService.getUser()).thenReturn(Optional.empty());

        this.mockMvc.perform(get(endpointCreditsMe))
            .andExpect(status().isForbidden());

        verifyNoInteractions(loggedInUserService);
    }

    @Test
    @WithMockUser("rob")
    void testMyFlagged_userIsFlagged() throws Exception {
        username = "rob";
        credits = -51.0f;
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));

        this.mockMvc.perform(get(endpointFlaggedMe)).andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(loggedInUserService, times(1)).getUser();
    }

    @Test
    @WithMockUser("chris")
    void testMyFlagged_userIsNotFlagged() throws Exception {
        username = "chris";
        credits = -49.9f;
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));

        this.mockMvc.perform(get(endpointFlaggedMe)).andExpect(status().isOk())
            .andExpect(content().string("false"));

        verify(loggedInUserService, times(1)).getUser();
    }

    @Test
    @WithMockUser
    void testMyFlagged_userDoesNotExist() throws Exception {
        when(loggedInUserService.getUser()).thenReturn(Optional.empty());

        this.mockMvc.perform(get(endpointFlaggedMe))
            .andExpect(status().isBadRequest());

        verify(loggedInUserService, times(1)).getUser();
    }

    @Test
    @WithAnonymousUser
    void testMyFlagged_userIsAnonymous() throws Exception {
        when(loggedInUserService.getUser()).thenReturn(Optional.empty());

        this.mockMvc.perform(get(endpointFlaggedMe))
            .andExpect(status().isForbidden());

        verifyNoInteractions(loggedInUserService);
    }

    @Test
    @WithMockUser()
    void testDeleteUser_success() {
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));
        when(httpCommunicationService.deleteUtility(endpointDeleteUser, port, testToken)).thenReturn(testResponseOk);

        userController.deleteUser("Bearer " + testToken);

        verify(loggedInUserService, times(1)).getUser();
        verify(userService, times(1)).deleteUser(user);
        verifyNoMoreInteractions(loggedInUserService, userService);
    }

    @Test
    @WithMockUser()
    void testDeleteUser_userDoesNotExist() {
        when(loggedInUserService.getUser()).thenReturn(Optional.empty());
        when(httpCommunicationService.deleteUtility(endpointDeleteUser, port, testToken)).thenReturn(testResponseOk);


        assertThrows(Exception.class, () ->
            userController.deleteUser("Bearer " + testToken));

        verify(loggedInUserService, times(1)).getUser();
        verifyNoMoreInteractions(loggedInUserService, userService);
    }

    @Test
    @WithMockUser()
    void testDeleteUser_invalidToken() {
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));
        when(httpCommunicationService.deleteUtility(endpointDeleteUser, port, testToken)).thenReturn(testResponseNotOk);

        assertThrows(ResponseStatusException.class,
                () -> userController.deleteUser("Bearer " + testToken));
    }

    @Test
    @WithMockUser()
    void testDeleteUser_nullToken() {
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));
        when(httpCommunicationService.deleteUtility(endpointDeleteUser, port, null)).thenReturn(testResponseNotOk);

        assertThrows(ResponseStatusException.class,
                () -> userController.deleteUser(null));
    }

    @Test
    @WithMockUser()
    void testDeleteUser_tokenDoesNotStartWithBearer() {
        UserEntity user = new UserEntity(username, credits);

        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));
        when(httpCommunicationService.deleteUtility(endpointDeleteUser, port, "Gearer " + testToken))
                .thenReturn(testResponseNotOk);

        assertThrows(ResponseStatusException.class,
                () -> userController.deleteUser("Gearer " + testToken));
    }
}