package com.alvaro.sistema.asignaturasservicio.service;

import com.alvaro.sistema.asignaturasservicio.model.Asignatura;
import com.alvaro.sistema.asignaturasservicio.repository.AsignaturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca como Servicio
public class AsignaturaServiceImpl implements AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    public AsignaturaServiceImpl(AsignaturaRepository asignaturaRepository) {
        this.asignaturaRepository = asignaturaRepository;
    }

    @Override
    public Asignatura createAsignatura(Asignatura asignatura) {
        return asignaturaRepository.save(asignatura); // Guarda en BD
    }

    @Override
    public List<Asignatura> getAllAsignaturas() {
        return asignaturaRepository.findAll(); // Obtiene todas
    }

    @Override
    public Optional<Asignatura> getAsignaturaById(String id) {
        return asignaturaRepository.findById(id); // Obtiene por ID
    }

}
