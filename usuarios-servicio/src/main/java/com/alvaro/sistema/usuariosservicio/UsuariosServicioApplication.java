package com.alvaro.sistema.usuariosservicio; // Este es el paquete raíz del módulo

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsuariosServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuariosServicioApplication.class, args);
    }

}