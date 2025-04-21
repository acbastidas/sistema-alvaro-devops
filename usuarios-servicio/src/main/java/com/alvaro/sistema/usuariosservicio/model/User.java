package com.alvaro.sistema.usuariosservicio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") // Indica que esta clase mapea a la colección 'users' en MongoDB
public class User {

    @Id // Indica que este campo es el ID del documento en MongoDB
    private String id; // MongoDB usa String para el ID por defecto, aunque puede ser ObjectId

    private String username;
    private String email;
    private String password; // Nota: En un sistema real, NUNCA almacenarías contraseñas en texto plano

    // Constructor vacío (necesario para Spring Data)
    public User() {
    }

    // Constructor con campos
    public User(String username, String email, String password) {
        this.username = username;
        ;
        this.email = email;
        this.password = password;
    }

    // --- Getters y Setters ---
    // Si usas Lombok, puedes reemplazar esto con anotaciones @Getter y @Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}'; // No mostrar la contraseña en toString()
    }
}