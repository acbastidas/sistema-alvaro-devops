package com.alvaro.sistema.usuariosservicio.repository;

import com.alvaro.sistema.usuariosservicio.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest // Anotación para pruebas de Spring Data MongoDB. Configura un contexto Spring
               // que incluye acceso a Mongo.
// @ActiveProfiles("test") // Opcional: Activa un perfil 'test' si necesitas
// configuraciones específicas para pruebas en src/test/resources
@DisplayName("Pruebas de Integración para UserRepository")
class UserRepositoryTest {

    @Autowired // Inyecta el repositorio real (la implementación de Spring Data)
    private UserRepository userRepository;

    // Nota: @DataMongoTest por defecto usa una base de datos en memoria (Embedded
    // MongoDB)
    // o se conecta a una instancia de MongoDB si está disponible y configurada para
    // tests.
    // Para asegurar un entorno de prueba limpio y consistente, a menudo se usa con
    // librerías
    // como Testcontainers para levantar un contenedor de Mongo específico para los
    // tests.
    // Por simplicidad, aquí asumimos que @DataMongoTest se configura correctamente.

    @Test
    @DisplayName("Debería guardar y encontrar un usuario")
    void shouldSaveAndFindUser() {
        // Arrange: Prepara un usuario
        User user = new User("testrepo", "repo@example.com", "password");

        // Act: Guarda el usuario en la BD
        User savedUser = userRepository.save(user);

        // Assert: Verifica que se guardó correctamente (tiene ID)
        assertNotNull(savedUser, "El usuario guardado no debería ser nulo");
        assertNotNull(savedUser.getId(), "El ID del usuario guardado no debería ser nulo");

        // Act: Busca el usuario guardado por su ID
        Optional<User> foundUserOptional = userRepository.findById(savedUser.getId());

        // Assert: Verifica que se encontró y los datos coinciden
        assertTrue(foundUserOptional.isPresent(), "El usuario guardado debería ser encontrado");
        User foundUser = foundUserOptional.get();
        assertEquals(savedUser.getId(), foundUser.getId(), "Los IDs deben coincidir");
        assertEquals(user.getUsername(), foundUser.getUsername(), "Los usernames deben coincidir");
        assertEquals(user.getEmail(), foundUser.getEmail(), "Los emails deben coincidir");
        // Nota: No verificamos la contraseña aquí por la misma razón que en los tests
        // unitarios.
    }

    @Test
    @DisplayName("Debería encontrar usuario por ID si existe")
    void shouldFindUserByIdIfExists() {
        // Arrange: Guarda un usuario directamente para este test
        User user = new User("findbyid", "find@example.com", "pass");
        User savedUser = userRepository.save(user);

        // Act: Busca por el ID del usuario guardado
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert: Verifica que se encontró
        assertTrue(foundUser.isPresent(), "El usuario debería ser encontrado por ID");
        assertEquals(savedUser.getId(), foundUser.get().getId(), "El ID debe coincidir");
    }

    @Test
    @DisplayName("No debería encontrar usuario por ID si no existe")
    void shouldNotFindUserByIdIfNotExists() {
        // Arrange: Prepara un ID que no existe
        String nonExistingId = "abcdef012345678901234567"; // Un ID con formato válido pero inexistente

        // Act: Busca por el ID inexistente
        Optional<User> foundUser = userRepository.findById(nonExistingId);

        // Assert: Verifica que no se encontró
        assertFalse(foundUser.isPresent(), "El usuario no debería ser encontrado");
    }

    @Test
    @DisplayName("Debería eliminar un usuario por ID")
    void shouldDeleteUserById() {
        // Arrange: Guarda un usuario para luego eliminarlo
        User user = new User("deleteuser", "delete@example.com", "deletepass");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId(), "El usuario debe guardarse con ID"); // Aseguramos que se guardó

        // Act: Elimina el usuario por su ID
        userRepository.deleteById(savedUser.getId());

        // Assert: Verifica que el usuario ya no existe
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent(), "El usuario no debería ser encontrado después de ser eliminado");
    }

    // Puedes añadir más tests aquí para otros métodos del repositorio si defines
    // algunos personalizados
    // (ej: findByUsername, findByEmail)
}