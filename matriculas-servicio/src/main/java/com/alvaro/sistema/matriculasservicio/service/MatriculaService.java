package com.alvaro.sistema.matriculasservicio.service;

import com.alvaro.sistema.matriculasservicio.model.Matricula;

public interface MatriculaService {

    Matricula registrarMatricula(String userId, String asignaturaId);

}