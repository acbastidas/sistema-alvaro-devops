package com.alvaro.sistema.asignaturasservicio.repository;

import com.alvaro.sistema.asignaturasservicio.model.Asignatura;
import org.springframework.data.mongodb.repository.MongoRepository;

// Extiende MongoRepository para CRUD automático
public interface AsignaturaRepository extends MongoRepository<Asignatura, String> {
    // Puedes añadir métodos personalizados aquí
}