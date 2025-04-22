package com.alvaro.sistema.matriculasservicio.controller;

import com.alvaro.sistema.matriculasservicio.model.Matricula;
import com.alvaro.sistema.matriculasservicio.service.MatriculaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    // Inyección de dependencias a través del constructor (Spring lo autowirea)
    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    // Endpoint para registrar una matrícula
    // @PostMapping mapea peticiones POST a /matriculas/registrar
    // @RequestParam extrae parámetros de la query string de la URL (ej:
    // ?userId=...&asignaturaId=...)
    @PostMapping("/registrar")
    public ResponseEntity<Matricula> registrarMatricula(@RequestParam String userId,
            @RequestParam String asignaturaId) {
        try {
            // Llama al método del servicio para ejecutar la lógica de registro
            Matricula nuevaMatricula = matriculaService.registrarMatricula(userId, asignaturaId);

            // Si el registro fue exitoso, retorna el objeto Matrícula creada
            // y establece el estado HTTP a 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMatricula);

        } catch (RuntimeException e) {
            // Aquí capturamos las excepciones lanzadas por el servicio (ej: usuario o
            // asignatura no encontrados)
            // En una aplicación real, podrías mapear diferentes tipos de RuntimeException
            // a estados HTTP más específicos (ej: ResourceNotFoundException -> 404 Not
            // Found).
            System.err.println("Error al registrar matrícula: " + e.getMessage()); // Log del error
            // Retorna un estado HTTP 400 Bad Request (solicitud inválida)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Puedes retornar null o un objeto con
                                                                             // mensaje de error
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada durante el proceso
            System.err.println("Error interno inesperado al registrar matrícula: " + e.getMessage()); // Log del error
            // Retorna un estado HTTP 500 Internal Server Error (error en el servidor)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna null o un objeto con
                                                                                       // mensaje de error
        }
    }

}
