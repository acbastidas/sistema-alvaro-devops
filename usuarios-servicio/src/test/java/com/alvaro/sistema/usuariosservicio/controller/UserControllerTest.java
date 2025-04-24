package com.alvaro.sistema.usuariosservicio.controller;

import com.alvaro.sistema.usuariosservicio.model.User;
import com.alvaro.sistema.usuariosservicio.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@EnableAutoConfiguration(exclude = {
        EurekaClientAutoConfiguration.class,
        SimpleDiscoveryClientAutoConfiguration.class,
        CompositeDiscoveryClientAutoConfiguration.class,
        ConfigServicePropertySourceLocator.class,
        ConfigurationPropertiesRebinderAutoConfiguration.class
})
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.config.import="
})
@DisplayName("Pruebas de Integración para UserController")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("POST /users - Debería crear un usuario y retornar 201")
    void shouldCreateUserAndReturn201() throws Exception {
        User newUser = new User("newuser", "new@example.com", "password");
        User createdUser = new User("newuser", "new@example.com", "password");
        createdUser.setId("mocked-id-123");

        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        String newUserJson = om.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("mocked-id-123"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    @DisplayName("GET /users - Debería obtener todos los usuarios y retornar 200")
    void shouldGetAllUsersAndReturn200() throws Exception {
        User user1 = new User("user1", "user1@example.com", "pass1");
        user1.setId("id1");
        User user2 = new User("user2", "user2@example.com", "pass2");
        user2.setId("id2");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @DisplayName("GET /users/{id} - Debería obtener usuario por ID existente y retornar 200")
    void shouldGetUserByIdExistingAndReturn200() throws Exception {
        String userId = "existing-id-456";
        User user = new User("existing", "existing@example.com", "pass");
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("existing"));
    }

    @Test
    @DisplayName("GET /users/{id} - Debería retornar 404 si usuario por ID no existe")
    void shouldReturn404IfUserByIdDoesNotExist() throws Exception {
        String userId = "non-existing-id-789";

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users/{id} - Debería actualizar usuario y retornar 200")
    void shouldUpdateUserAndReturn200() throws Exception {
        String userId = "update-id-abc";
        User updatedUserDetails = new User("updated", "updated@example.com", "newpass");
        User updatedUserAfterService = new User("updated", "updated@example.com", "newpass");
        updatedUserAfterService.setId(userId);

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUserAfterService);

        String updatedUserJson = om.writeValueAsString(updatedUserDetails);

        mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("updated"));
    }

    @Test
    @DisplayName("DELETE /users/{id} - Debería eliminar usuario y retornar 204")
    void shouldDeleteUserAndReturn204() throws Exception {
        String userIdToDelete = "delete-id-def";
        mockMvc.perform(delete("/users/{id}", userIdToDelete))
                .andExpect(status().isNoContent());
    }
}