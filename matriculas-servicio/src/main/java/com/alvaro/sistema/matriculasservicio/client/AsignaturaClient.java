package com.alvaro.sistema.matriculasservicio.client;

import com.alvaro.sistema.matriculasservicio.model.Asignatura; // Importa el modelo local de Asignatura
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "asignaturas-servicio")
public interface AsignaturaClient {

    @GetMapping("/asignaturas/{id}")
    Optional<Asignatura> getAsignaturaById(@PathVariable("id") String id);

}