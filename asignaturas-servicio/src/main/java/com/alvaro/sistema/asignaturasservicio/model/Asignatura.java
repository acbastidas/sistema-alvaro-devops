package com.alvaro.sistema.asignaturasservicio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "asignaturas") // Mapea a la colección "asignaturas" en MongoDB
public class Asignatura {
    @Id // ID del documento
    private String id;

    private String nombre;
    private String codigo;
    // Añade otros campos relevantes si los tienes

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // Constructor por defecto
    public Asignatura() {
    }

    // Constructor con campos (sin ID)
    public Asignatura(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // Puedes añadir toString(), equals(), hashCode()
}