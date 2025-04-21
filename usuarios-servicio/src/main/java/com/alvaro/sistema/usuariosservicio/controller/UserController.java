package com.alvaro.sistema.usuariosservicio.controller;

import com.alvaro.sistema.usuariosservicio.model.User;
import com.alvaro.sistema.usuariosservicio.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que es un controlador REST
@RequestMapping("/users") // Define el path base para los endpoints de este controlador
public class UserController {

    private final UserService userService;

    // Inyección de dependencia del UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping // Maneja peticiones POST a /users
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED); // Retorna 201 Created
    }

    @GetMapping // Maneja peticiones GET a /users
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // Retorna 200 OK
    }

    @GetMapping("/{id}") // Maneja peticiones GET a /users/{id}
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok) // Si el usuario existe, retorna 200 OK con el usuario
                .orElse(ResponseEntity.notFound().build()); // Si no, retorna 404 Not Found
    }

    @PutMapping("/{id}") // Maneja peticiones PUT a /users/{id}
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        // La lógica en el servicio devuelve el usuario actualizado o uno nuevo si no
        // existía
        // Podrías refinar esto para retornar 404 si no existía originalmente si lo
        // prefieres
        return ResponseEntity.ok(updatedUser); // Retorna 200 OK con el usuario actualizado/creado
    }

    @DeleteMapping("/{id}") // Maneja peticiones DELETE a /users/{id}
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}