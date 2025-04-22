package com.alvaro.sistema.matriculasservicio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matriculas") // Indica que esta clase mapea a una colección llamada "matriculas" en MongoDB
public class Matricula {
    @Id // Marca este campo como el ID principal del documento en MongoDB
    private String id;

    private String userId; // ID del usuario matriculado (obtenido de usuarios-servicio)
    private String asignaturaId; // ID de la asignatura matriculada (obtenido de asignaturas-servicio)
    private String fechaMatricula; // Fecha de la matrícula (lo guardamos como String por simplicidad aquí)

    // Getters y Setters (para acceder y modificar los campos)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(String asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public String getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(String fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    // Constructor por defecto (necesario para Spring Data y deserialización)
    public Matricula() {
    }

    // Constructor con campos (sin ID, ya que MongoDB lo genera)
    public Matricula(String userId, String asignaturaId, String fechaMatricula) {
        this.userId = userId;
        this.asignaturaId = asignaturaId;
        this.fechaMatricula = fechaMatricula;
    }
}