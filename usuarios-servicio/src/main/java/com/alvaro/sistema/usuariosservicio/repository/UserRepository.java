package com.alvaro.sistema.usuariosservicio.repository;

import com.alvaro.sistema.usuariosservicio.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
