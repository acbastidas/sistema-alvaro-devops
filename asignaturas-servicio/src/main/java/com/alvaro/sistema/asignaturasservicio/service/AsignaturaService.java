package com.alvaro.sistema.asignaturasservicio.service;

import com.alvaro.sistema.asignaturasservicio.model.Asignatura;

import java.util.List;
import java.util.Optional;

public interface AsignaturaService {
    Asignatura createAsignatura(Asignatura asignatura); // Para crear una asignatura (necesario para POST)

    List<Asignatura> getAllAsignaturas(); // Para obtener todas

    Optional<Asignatura> getAsignaturaById(String id); // ¡Necesario para la llamada Feign desde Matrículas!

}
