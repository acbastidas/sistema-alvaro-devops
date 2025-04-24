package com.alvaro.sistema.usuariosservicio.service;

import com.alvaro.sistema.usuariosservicio.model.User;
import com.alvaro.sistema.usuariosservicio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - UserServiceImpl")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Debe crear un usuario exitosamente")
    void shouldCreateUserSuccessfully() {
        User userToCreate = new User("testuser", "test@example.com", "password");
        User createdUser = new User("testuser", "test@example.com", "password");
        createdUser.setId("mocked-id-123");

        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        User result = userService.createUser(userToCreate);

        assertNotNull(result);
        assertEquals("mocked-id-123", result.getId());
        assertEquals("testuser", result.getUsername());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void shouldGetAllUsers() {
        User user1 = new User("user1", "user1@example.com", "pass1");
        User user2 = new User("user2", "user2@example.com", "pass2");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener un usuario por ID existente")
    void shouldGetUserByIdExisting() {
        String userId = "existing-id-456";
        User user = new User("existing", "existing@example.com", "pass");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("No debe obtener un usuario por ID inexistente")
    void shouldNotGetUserByIdNonExisting() {
        String userId = "non-existing-id-789";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(userId);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente exitosamente")
    void shouldUpdateExistingUserSuccessfully() {
        String userId = "update-id-abc";
        User existingUser = new User("olduser", "old@example.com", "oldpass");
        existingUser.setId(userId);

        User updatedDetails = new User("newuser", "new@example.com", "newpass");
        User updatedUser = new User("newuser", "new@example.com", "newpass");
        updatedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedDetails);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newpass", result.getPassword());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Debe crear un nuevo usuario si no existe el ID al actualizar")
    void shouldCreateNewUserIfIdNotFoundWhenUpdating() {
        String nonExistingId = "non-existing-id-xyz";
        User userDetails = new User("newuser", "new@example.com", "newpass");

        User newUser = new User("newuser", "new@example.com", "newpass");
        newUser.setId(nonExistingId);

        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.updateUser(nonExistingId, userDetails);

        assertNotNull(result);
        assertEquals(nonExistingId, result.getId());
        assertEquals("newuser", result.getUsername());

        verify(userRepository).findById(nonExistingId);
        verify(userRepository).save(argThat(user -> nonExistingId.equals(user.getId())));
    }

    @Test
    @DisplayName("Debe eliminar un usuario por ID")
    void shouldDeleteUserById() {
        String userId = "delete-id-def";

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}
