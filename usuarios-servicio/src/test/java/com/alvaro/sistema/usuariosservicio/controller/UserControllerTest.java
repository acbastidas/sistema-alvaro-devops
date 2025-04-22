package com.alvaro.sistema.usuariosservicio.controller;

import com.alvaro.sistema.usuariosservicio.model.User;
import com.alvaro.sistema.usuariosservicio.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) // Configura un contexto Spring para probar solo la capa web, enfocándose en
                                  // UserController.
@DisplayName("Pruebas de Integración para UserController")
class UserControllerTest {

    @Autowired // Inyecta MockMvc para simular peticiones HTTP
    private MockMvc mockMvc;

    @MockBean // Crea un mock de UserService y lo inyecta en el controlador
    private UserService userService;

    @Test
    @DisplayName("POST /users - Debería crear un usuario y retornar 201")
    void shouldCreateUserAndReturn201() throws Exception {
        // Arrange: Prepara el usuario que se enviará y el usuario que el servicio
        // simulará devolver
        User createdUser = new User("newuser", "new@example.com", "password");
        createdUser.setId("mocked-id-123");

        // Configura el mock del servicio: cuando se llame a createUser con cualquier
        // User, devuelve createdUser
        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        // Act & Assert: Realiza la petición simulada y verifica la respuesta
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON) // Indica que el cuerpo es JSON
                .content("{\"username\":\"newuser\",\"email\":\"new@example.com\",\"password\":\"password\"}")) // Cuerpo
                                                                                                                // de la
                                                                                                                // petición
                                                                                                                // en
                                                                                                                // formato
                                                                                                                // JSON
                .andExpect(status().isCreated()) // Verifica que el estado HTTP sea 201
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica que la respuesta sea JSON
                .andExpect(jsonPath("$.id").value("mocked-id-123")) // Verifica el campo 'id' en el JSON de respuesta
                .andExpect(jsonPath("$.username").value("newuser")); // Verifica otro campo

        // Opcional: Puedes verificar que el método del servicio fue llamado con el
        // argumento correcto
        // verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("GET /users - Debería obtener todos los usuarios y retornar 200")
    void shouldGetAllUsersAndReturn200() throws Exception {
        // Arrange: Prepara una lista de usuarios simulada que el servicio devolverá
        User user1 = new User("user1", "user1@example.com", "pass1");
        User user2 = new User("user2", "user2@example.com", "pass2");
        List<User> users = Arrays.asList(user1, user2);

        // Configura el mock del servicio: cuando se llame a getAllUsers, devuelve la
        // lista simulada
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert: Realiza la petición simulada y verifica la respuesta
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)) // Indica que acepta respuesta JSON
                .andExpect(status().isOk()) // Verifica que el estado HTTP sea 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2)) // Verifica que la lista tiene 2 elementos
                .andExpect(jsonPath("$[0].username").value("user1")) // Verifica campos del primer elemento
                .andExpect(jsonPath("$[1].username").value("user2")); // Verifica campos del segundo elemento
    }

    @Test
    @DisplayName("GET /users/{id} - Debería obtener usuario por ID existente y retornar 200")
    void shouldGetUserByIdExistingAndReturn200() throws Exception {
        // Arrange: Prepara un ID y un usuario simulado que el servicio devolverá
        String userId = "existing-id-456";
        User user = new User("existing", "existing@example.com", "pass");
        user.setId(userId);

        // Configura el mock del servicio: cuando se llame a getUserById con el ID,
        // devuelve un Optional con el usuario
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // Act & Assert: Realiza la petición simulada y verifica la respuesta
        mockMvc.perform(get("/users/{id}", userId) // Pasa el ID como variable de path
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId)) // Verifica campos del usuario devuelto
                .andExpect(jsonPath("$.username").value("existing"));
    }

    @Test
    @DisplayName("GET /users/{id} - Debería retornar 404 si usuario por ID no existe")
    void shouldReturn404IfUserByIdDoesNotExist() throws Exception {
        // Arrange: Prepara un ID inexistente
        String userId = "non-existing-id-789";

        // Configura el mock del servicio: cuando se llame a getUserById con el ID,
        // devuelve un Optional vacío
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert: Realiza la petición simulada y verifica la respuesta
        mockMvc.perform(get("/users/{id}", userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Verifica estado 404
    }

    @Test
    @DisplayName("PUT /users/{id} - Debería actualizar usuario y retornar 200")
    void shouldUpdateUserAndReturn200() throws Exception {
        // Arrange: Prepara ID, detalles actualizados que se enviarán, y el usuario
        // simulado que el servicio devolverá después de actualizar
        String userId = "update-id-abc";
        User updatedUserAfterService = new User("updated", "updated@example.com", "newpass");
        updatedUserAfterService.setId(userId);

        // Configura el mock del servicio: cuando se llame a updateUser con el ID y
        // cualquier User, devuelve el usuario actualizado simulado
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUserAfterService);

        // Act & Assert: Realiza la petición simulada y verifica la respuesta
        mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"updated\",\"email\":\"updated@example.com\",\"password\":\"newpass\"}")) // Cuerpo
                                                                                                                   // de
                                                                                                                   // la
                                                                                                                   // petición
                                                                                                                   // PUT
                .andExpect(status().isOk()) // Verifica 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("updated"));
    }

    @Test
    @DisplayName("DELETE /users/{id} - Debería eliminar usuario y retornar 204")
    void shouldDeleteUserAndReturn204() throws Exception {
        // Arrange: Prepara el ID del usuario a eliminar
        String userIdToDelete = "delete-id-def";

        // Configura el mock del servicio: cuando se llame a deleteUser, no hace nada
        // (void method)
        // doNothing().when(userService).deleteUser(userIdToDelete); // Opcional, si
        // quieres ser explícito

        // Act & Assert: Realiza la petición simulada y verifica la respuesta
        mockMvc.perform(delete("/users/{id}", userIdToDelete))
                .andExpect(status().isNoContent()); // Verifica estado 204
    }
}