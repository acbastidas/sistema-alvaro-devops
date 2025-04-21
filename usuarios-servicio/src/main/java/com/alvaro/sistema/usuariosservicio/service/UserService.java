package com.alvaro.sistema.usuariosservicio.service;

import com.alvaro.sistema.usuariosservicio.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(String id);

    User updateUser(String id, User userDetails);

    void deleteUser(String id);
}