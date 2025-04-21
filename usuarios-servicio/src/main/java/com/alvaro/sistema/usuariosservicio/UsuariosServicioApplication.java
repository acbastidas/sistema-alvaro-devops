package com.alvaro.sistema.usuariosservicio; // Este es el paquete raíz del módulo

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Esta anotación marca la clase principal y habilita Spring Boot features
public class UsuariosServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuariosServicioApplication.class, args);
    }

}