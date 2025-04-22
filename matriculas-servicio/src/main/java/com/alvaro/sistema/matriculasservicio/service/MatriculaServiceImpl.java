package com.alvaro.sistema.matriculasservicio.service;

import com.alvaro.sistema.matriculasservicio.client.AsignaturaClient;
import com.alvaro.sistema.matriculasservicio.client.UserClient;
import com.alvaro.sistema.matriculasservicio.model.Asignatura;
import com.alvaro.sistema.matriculasservicio.model.Matricula;
import com.alvaro.sistema.matriculasservicio.model.User;
import com.alvaro.sistema.matriculasservicio.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UserClient userClient; // Inyección del Feign Client de Usuario
    private final AsignaturaClient asignaturaClient; // Inyección del Feign Client de Asignatura

    // Inyección de dependencias a través del constructor
    // Spring detectará este constructor (único) e inyectará las dependencias
    // necesarias
    public MatriculaServiceImpl(MatriculaRepository matriculaRepository,
            UserClient userClient, // Spring inyectará la implementación automática de Feign
            AsignaturaClient asignaturaClient) {
        this.matriculaRepository = matriculaRepository;
        this.userClient = userClient;
        this.asignaturaClient = asignaturaClient;
    }

    @Override
    public Matricula registrarMatricula(String userId, String asignaturaId) {
        // --- Lógica para simular la operación completa de registro de matrícula ---

        // 1. Validar si el usuario existe llamando a usuarios-servicio a través del
        // Feign Client
        System.out.println("Intentando obtener Usuario con ID: " + userId); // Log para depuración
        Optional<User> userOptional = userClient.getUserById(userId);

        // Si el usuario no se encuentra, lanzamos una excepción
        if (!userOptional.isPresent()) {
            System.err.println("Error: Usuario no encontrado con ID: " + userId);
            // En una aplicación real, podrías lanzar una excepción más específica y
            // manejarla en el controlador
            throw new RuntimeException("Usuario no encontrado: " + userId);
        }
        User user = userOptional.get(); // Obtenemos el objeto User si existe
        System.out.println("Usuario encontrado: " + user.getUsername()); // Log de éxito

        // 2. Validar si la asignatura existe llamando a asignaturas-servicio a través
        // del Feign Client
        System.out.println("Intentando obtener Asignatura con ID: " + asignaturaId); // Log para depuración
        Optional<Asignatura> asignaturaOptional = asignaturaClient.getAsignaturaById(asignaturaId);

        // Si la asignatura no se encuentra, lanzamos una excepción
        if (!asignaturaOptional.isPresent()) {
            System.err.println("Error: Asignatura no encontrada con ID: " + asignaturaId);
            // En una aplicación real, podrías lanzar una excepción más específica
            throw new RuntimeException("Asignatura no encontrada: " + asignaturaId);
        }
        Asignatura asignatura = asignaturaOptional.get(); // Obtenemos el objeto Asignatura si existe
        System.out.println("Asignatura encontrada: " + asignatura.getNombre()); // Log de éxito

        // 3. Crear una nueva entidad de Matrícula con los IDs y la fecha y hora actual
        Matricula nuevaMatricula = new Matricula();
        nuevaMatricula.setUserId(userId);
        nuevaMatricula.setAsignaturaId(asignaturaId);
        // Obtenemos la fecha y hora actual y la formateamos a un String ISO
        nuevaMatricula.setFechaMatricula(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // 4. Guardar la nueva matrícula en la base de datos de matriculas-servicio
        System.out.println("Guardando nueva matrícula en la BD..."); // Log para depuración
        Matricula matriculaGuardada = matriculaRepository.save(nuevaMatricula);
        System.out.println("Matrícula registrada exitosamente con ID: " + matriculaGuardada.getId()); // Log de éxito

        // 5. Retornar la matrícula guardada (ahora tiene el ID generado por MongoDB)
        return matriculaGuardada;
    }

}