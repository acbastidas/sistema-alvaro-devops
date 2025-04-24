package com.alvaro.sistema.usuariosservicio.repository;

import com.alvaro.sistema.usuariosservicio.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.cloud.config.enabled=false")
@DisplayName("Pruebas de Integración para UserRepository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debería guardar y encontrar un usuario")
    void shouldSaveAndFindUser() {
        User user = new User("testrepo", "repo@example.com", "password");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Debería encontrar usuario por ID si existe")
    void shouldFindUserByIdIfExists() {
        User user = new User("findbyid", "find@example.com", "pass");
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    @DisplayName("No debería encontrar usuario por ID si no existe")
    void shouldNotFindUserByIdIfNotExists() {
        String nonExistingId = "abcdef012345678901234567";
        Optional<User> foundUser = userRepository.findById(nonExistingId);
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Debería eliminar un usuario por ID")
    void shouldDeleteUserById() {
        User user = new User("deleteuser", "delete@example.com", "deletepass");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }
}
