package com.alvaro.sistema.asignaturasservicio.controller;

import com.alvaro.sistema.asignaturasservicio.model.Asignatura;
import com.alvaro.sistema.asignaturasservicio.service.AsignaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Controlador REST
@RequestMapping("/asignaturas") // Path base
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    public AsignaturaController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }

    // Endpoint para crear una nueva asignatura
    @PostMapping // Mapea POST a /asignaturas
    public ResponseEntity<Asignatura> createAsignatura(@RequestBody Asignatura asignatura) {
        Asignatura createdAsignatura = asignaturaService.createAsignatura(asignatura);
        // Retorna la asignatura creada con estado 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsignatura);
    }

    // Endpoint para obtener todas las asignaturas
    @GetMapping // Mapea GET a /asignaturas
    public ResponseEntity<List<Asignatura>> getAllAsignaturas() {
        List<Asignatura> asignaturas = asignaturaService.getAllAsignaturas();
        return ResponseEntity.ok(asignaturas); // Retorna lista con 200 OK
    }

    // Endpoint para obtener una asignatura por ID
    @GetMapping("/{id}") // Mapea GET a /asignaturas/{id}. ¡Este es el endpoint que Matriculas necesita!
    public ResponseEntity<Asignatura> getAsignaturaById(@PathVariable String id) {
        Optional<Asignatura> asignatura = asignaturaService.getAsignaturaById(id);
        // Si se encuentra, retorna 200 OK; si no, 404 Not Found
        return asignatura.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
