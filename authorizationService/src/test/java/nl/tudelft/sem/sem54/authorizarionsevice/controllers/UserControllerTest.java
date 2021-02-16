package nl.tudelft.sem.sem54.authorizarionsevice.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import nl.tudelft.sem.sem54.authorizarionsevice.repositories.UserRepository;
import nl.tudelft.sem.sem54.authorizarionsevice.services.LoggedInUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
//@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient UserRepository userRepository;

    @MockBean
    private transient LoggedInUserService loggedInUserService;

    public static final MediaType APPLICATION_JSON_UTF8 =
        new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private transient String endpointUser;
    private transient String endpointDelete;
    private transient String username;
    private transient String password;

    @BeforeEach
    void beforeEach() {
        endpointUser = "/auth/user";
        endpointDelete = "/auth/user/me";
        username = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString();
    }

    private String makeContent(String username, String password) {
        return "{\"username\":\""
            + username
            + "\",\"password\":\""
            + password
            + "\"}";
    }

    @Test
    @WithMockUser
    void testGetAllUsers_success() throws Exception {
        UserEntity user = new UserEntity(username, password);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);
        when(userRepository.findAll()).thenReturn(iterable);
        this.mockMvc.perform(get(endpointUser)).andExpect(status().isOk())
            .andExpect(content().string(
                containsString("[{\"username\":\""
                    + username
                    + "\",\"password\":\""
                    + password
                    + "\"}]")));
    }

    @Test
    void testGetAllUsers_fails() throws Exception {
        UserEntity user = new UserEntity(username, password);
        ArrayList<UserEntity> iterable = new ArrayList<>();
        iterable.add(user);
        when(userRepository.findAll()).thenReturn(iterable);
        this.mockMvc.perform(get(endpointUser)).andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateUser_success() throws Exception {
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        this.mockMvc.perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
            .content(makeContent(username, password)))
            .andExpect(status().isOk());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_userAlreadyExists() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(
            Optional.of(new UserEntity(username, password)));


        this.mockMvc
            .perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
                .content(makeContent(username, password)))
            .andExpect(status().isConflict());
    }

    @Test
    void testCreateUser_nullUsername() throws Exception {
        this.mockMvc
            .perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
                .content("{\"password\":\""
                    + password
                    + "\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_nullPassword() throws Exception {
        this.mockMvc
            .perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
                .content("{\"username\":\""
                    + username
                    + "\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createNull() throws Exception {
        this.mockMvc
            .perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_emptyUsername() throws Exception {
        this.mockMvc
            .perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
                .content(makeContent("", password)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_emptyPassword() throws Exception {
        this.mockMvc
            .perform(post(endpointUser).contentType(APPLICATION_JSON_UTF8)
                .content(makeContent(username, "")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser()
    void testDeleteUser_success() throws Exception {
        UserEntity user = new UserEntity();
        when(loggedInUserService.getUser()).thenReturn(Optional.of(user));
        this.mockMvc
            .perform(delete(endpointDelete).contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        verify(loggedInUserService, times(1)).getUser();
        verify(userRepository, times(1)).delete(user);
        verifyNoMoreInteractions(loggedInUserService, userRepository);
    }

    @Test
    @WithMockUser()
    void testDeleteUser_userDoesNotExist() throws Exception {
        when(loggedInUserService.getUser()).thenReturn(Optional.empty());
        this.mockMvc
            .perform(delete(endpointDelete).contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
        verify(loggedInUserService, times(1)).getUser();
        verifyNoMoreInteractions(loggedInUserService, userRepository);
    }

    @Test
    @WithAnonymousUser
    void testDeleteUser_userIsAnonymous() throws Exception {
        this.mockMvc
            .perform(delete(endpointDelete).contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isForbidden());
        verifyNoInteractions(loggedInUserService, userRepository);
    }
}