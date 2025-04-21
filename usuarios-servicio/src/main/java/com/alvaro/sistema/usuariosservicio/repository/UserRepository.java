package com.alvaro.sistema.usuariosservicio.repository;

import com.alvaro.sistema.usuariosservicio.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que es un componente de repositorio gestionado por Spring
public interface UserRepository extends MongoRepository<User, String> {
    // MongoRepository<TipoDeObjeto, TipoDelID>

    // Spring Data permite definir métodos de consulta personalizados
    // basándose en el nombre del método. Ejemplo:
    // Optional<User> findByUsername(String username);
    // List<User> findByEmailContaining(String email);
}
