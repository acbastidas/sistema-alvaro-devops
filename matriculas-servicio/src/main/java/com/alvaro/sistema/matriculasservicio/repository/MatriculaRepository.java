package com.alvaro.sistema.matriculasservicio.repository;

import com.alvaro.sistema.matriculasservicio.model.Matricula;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatriculaRepository extends MongoRepository<Matricula, String> {

}