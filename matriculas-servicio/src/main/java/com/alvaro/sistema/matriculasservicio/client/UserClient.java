package com.alvaro.sistema.matriculasservicio.client;

import com.alvaro.sistema.matriculasservicio.model.User; // Importa el modelo local de User
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "usuarios-servicio")
public interface UserClient {

    @GetMapping("/users/{id}")
    Optional<User> getUserById(@PathVariable("id") String id);

}