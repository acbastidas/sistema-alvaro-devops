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

@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
@DisplayName("Pruebas Unitarias para UserService") // Nombre descriptivo para el conjunto de pruebas
class UserServiceImplTest {

    @Mock // Crea un mock del UserRepository
    private UserRepository userRepository;

    @InjectMocks // Inyecta los mocks (UserRepository) en esta instancia de UserServiceImpl
    private UserServiceImpl userService;

    // Opcional: Método que se ejecuta antes de cada test si necesitas configuración
    // común
    @BeforeEach
    void setUp() {
        // Aquí podrías resetear mocks o inicializar datos si fuera necesario para todos
        // los tests
    }

    @Test // Marca este método como un test
    @DisplayName("Debería crear un usuario exitosamente") // Nombre descriptivo del test
    void shouldCreateUserSuccessfully() {
        // Arrange (Preparar): Configura el mock y el objeto a probar
        User userToCreate = new User("testuser", "test@example.com", "password");
        User createdUser = new User("testuser", "test@example.com", "password"); // Simula el objeto guardado (con ID si
                                                                                 // se generara)
        createdUser.setId("mocked-id-123"); // Asigna un ID simulado

        // Define el comportamiento del mock: cuando se llame a save con cualquier User,
        // devuelve createdUser
        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        // Act (Actuar): Ejecuta el método del servicio que quieres probar
        User result = userService.createUser(userToCreate);

        // Assert (Verificar): Comprueba el resultado y la interacción con el mock
        assertNotNull(result, "El usuario creado no debería ser nulo");
        assertEquals("mocked-id-123", result.getId(), "El ID del usuario creado no coincide");
        assertEquals(userToCreate.getUsername(), result.getUsername(), "El username no coincide");

        // Verifica que el método save del repositorio fue llamado exactamente una vez
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debería obtener todos los usuarios")
    void shouldGetAllUsers() {
        // Arrange: Prepara una lista de usuarios simulada
        User user1 = new User("user1", "user1@example.com", "pass1");
        User user2 = new User("user2", "user2@example.com", "pass2");
        List<User> users = Arrays.asList(user1, user2);

        // Define el comportamiento del mock: cuando se llame a findAll, devuelve la
        // lista simulada
        when(userRepository.findAll()).thenReturn(users);

        // Act: Llama al método del servicio
        List<User> result = userService.getAllUsers();

        // Assert: Verifica el resultado
        assertNotNull(result, "La lista de usuarios no debería ser nula");
        assertEquals(2, result.size(), "La lista debería contener 2 usuarios");
        assertTrue(result.contains(user1), "La lista debería contener user1");
        assertTrue(result.contains(user2), "La lista debería contener user2");

        // Verifica que el método findAll del repositorio fue llamado
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería obtener un usuario por ID existente")
    void shouldGetUserByIdExisting() {
        // Arrange: Prepara un usuario simulado con un ID
        String userId = "existing-id-456";
        User user = new User("existing", "existing@example.com", "pass");
        user.setId(userId);

        // Define el comportamiento del mock: cuando se llame a findById con el ID,
        // devuelve un Optional con el usuario
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act: Llama al método del servicio
        Optional<User> result = userService.getUserById(userId);

        // Assert: Verifica el resultado
        assertTrue(result.isPresent(), "El usuario debería estar presente");
        assertEquals(userId, result.get().getId(), "El ID del usuario obtenido no coincide");

        // Verifica que el método findById del repositorio fue llamado
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("No debería obtener un usuario por ID inexistente")
    void shouldNotGetUserByIdNonExisting() {
        // Arrange: Prepara un ID que no existe
        String userId = "non-existing-id-789";

        // Define el comportamiento del mock: cuando se llame a findById con el ID,
        // devuelve un Optional vacío
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act: Llama al método del servicio
        Optional<User> result = userService.getUserById(userId);

        // Assert: Verifica el resultado
        assertFalse(result.isPresent(), "El usuario no debería estar presente");

        // Verifica que el método findById del repositorio fue llamado
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente exitosamente")
    void shouldUpdateExistingUserSuccessfully() {
        // Arrange: Prepara un ID, un usuario existente simulado y detalles actualizados
        String userId = "update-id-abc";
        User existingUser = new User("olduser", "old@example.com", "oldpass");
        existingUser.setId(userId);

        User updatedDetails = new User("newuser", "new@example.com", "newpass"); // Simula datos que vienen en la
                                                                                 // petición
        // Si en la petición solo viniera {"email": "new@example.com"}, los otros campos
        // serían null.
        // Nuestra lógica de updateUser mejorada maneja esto. Aquí simulamos que vienen
        // todos los campos.

        User userAfterUpdateAndSave = new User("newuser", "new@example.com", "newpass");
        userAfterUpdateAndSave.setId(userId);

        // Configura el mock para findById (encontrar el usuario existente)
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        // Configura el mock para save (guardar el usuario actualizado)
        // Capturamos el argumento pasado a save para verificar sus campos
        when(userRepository.save(any(User.class))).thenReturn(userAfterUpdateAndSave);

        // Act: Llama al método del servicio updateUser
        User result = userService.updateUser(userId, updatedDetails);

        // Assert: Verifica el resultado
        assertNotNull(result, "El usuario actualizado no debería ser nulo");
        assertEquals(userId, result.getId(), "El ID del usuario actualizado no coincide");
        assertEquals(updatedDetails.getUsername(), result.getUsername(), "El username actualizado no coincide");
        assertEquals(updatedDetails.getEmail(), result.getEmail(), "El email actualizado no coincide");
        assertEquals(updatedDetails.getPassword(), result.getPassword(), "La contraseña actualizada no coincide"); // Recuerda
                                                                                                                   // la
                                                                                                                   // nota
                                                                                                                   // sobre
                                                                                                                   // contraseñas

        // Verifica que findById fue llamado una vez con el ID correcto
        verify(userRepository, times(1)).findById(userId);
        // Verifica que save fue llamado una vez
        verify(userRepository, times(1)).save(any(User.class)); // O puedes verificar que se guardó el objeto
                                                                // 'existingUser' con los campos actualizados

    }

    // Nota: Para el escenario donde updateUser crea un nuevo usuario (cuando el ID
    // no existe),
    // también deberías escribir un test que verifique ese comportamiento según tu
    // lógica.
    // Por ejemplo:
    @Test
    @DisplayName("Debería crear un nuevo usuario si el ID no existe al actualizar")
    void shouldCreateNewUserIfIdNotFoundWhenUpdating() {
        // Arrange: Prepara un ID inexistente y detalles para el nuevo usuario
        String nonExistingId = "non-existing-id-xyz";
        User userDetailsForNew = new User("newuser", "new@example.com", "newpass");

        User newUserAfterSave = new User("newuser", "new@example.com", "newpass");
        newUserAfterSave.setId(nonExistingId); // Simula el ID asignado por la lógica del servicio

        // Configura el mock para findById: devuelve Optional vacío (usuario no
        // encontrado)
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        // Configura el mock para save: devuelve el nuevo usuario simulado
        when(userRepository.save(any(User.class))).thenReturn(newUserAfterSave);

        // Act: Llama al método del servicio updateUser con el ID inexistente
        User result = userService.updateUser(nonExistingId, userDetailsForNew);

        // Assert: Verifica el resultado
        assertNotNull(result, "El usuario creado no debería ser nulo");
        assertEquals(nonExistingId, result.getId(), "El ID del nuevo usuario debería ser el de la URL");
        assertEquals(userDetailsForNew.getUsername(), result.getUsername(),
                "El username del nuevo usuario no coincide");

        // Verifica que findById fue llamado una vez con el ID correcto
        verify(userRepository, times(1)).findById(nonExistingId);
        // Verifica que save fue llamado una vez
        // Opcional: verifica que el objeto pasado a save tenga el ID asignado
        verify(userRepository, times(1)).save(argThat(user -> nonExistingId.equals(user.getId())));
    }

    @Test
    @DisplayName("Debería eliminar un usuario existente por ID")
    void shouldDeleteUserById() {
        // Arrange: Prepara un ID de usuario a eliminar
        String userIdToDelete = "delete-id-def";

        // No necesitamos mockear un retorno específico para deleteById si no devuelve
        // nada (void)
        // Solo nos aseguramos de que el método del mock sea llamado.
        // Aunque puedes mockearlo si lanzara una excepción en ciertas condiciones y
        // quieres probar eso.

        // Act: Llama al método del servicio
        userService.deleteUser(userIdToDelete);

        // Assert: Verifica que el método deleteById del repositorio fue llamado
        // exactamente una vez con el ID correcto
        verify(userRepository, times(1)).deleteById(userIdToDelete);
    }

    // Nota: Para el escenario de deleteUser si el ID no existe, el método
    // deleteById de Spring Data
    // por defecto no lanza una excepción si el ID no se encuentra. Si tu lógica
    // requiere un
    // comportamiento específico (ej: lanzar 404), deberías añadir ese test.
}