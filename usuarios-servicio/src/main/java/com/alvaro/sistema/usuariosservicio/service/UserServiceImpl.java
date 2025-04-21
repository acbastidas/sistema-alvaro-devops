package com.alvaro.sistema.usuariosservicio.service;

import com.alvaro.sistema.usuariosservicio.model.User;
import com.alvaro.sistema.usuariosservicio.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que es un componente de servicio gestionado por Spring
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Inyección de dependencia del UserRepository
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        // Aquí podrías añadir lógica de validación o negocio antes de guardar
        // Por ejemplo: verificar si el username o email ya existen
        return userRepository.save(user); // .save() es proporcionado por MongoRepository
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(); // .findAll() es proporcionado por MongoRepository
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id); // .findById() es proporcionado por MongoRepository
    }

    @Override
    public User updateUser(String id, User userDetails) {
        // Aquí implementamos la lógica de actualización
        return userRepository.findById(id)
                .map(user -> {
                    // Actualiza los campos del usuario existente con los detalles proporcionados
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword()); // Recuerda la nota sobre contraseñas
                    // Guarda el usuario actualizado (Spring Data actualiza si el ID existe)
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    // Si el usuario no existe, podrías lanzar una excepción o crear uno nuevo
                    // Aquí optamos por crear uno nuevo (aunque podrías cambiar esta lógica)
                    userDetails.setId(id); // Asigna el ID proporcionado al nuevo usuario
                    return userRepository.save(userDetails);
                });
    }

    @Override
    public void deleteUser(String id) {
        // Aquí podrías añadir lógica de negocio antes de eliminar
        userRepository.deleteById(id); // .deleteById() es proporcionado por MongoRepository
    }
}