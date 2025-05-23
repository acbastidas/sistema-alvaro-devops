package com.alvaro.sistema.matriculasservicio.model;

public class Asignatura {
    private String id;
    private String nombre;
    private String codigo;

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

    // Constructor con campos
    public Asignatura(String id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }

}