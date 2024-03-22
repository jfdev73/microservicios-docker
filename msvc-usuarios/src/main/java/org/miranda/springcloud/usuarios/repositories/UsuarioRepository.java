package org.miranda.springcloud.usuarios.repositories;

import org.miranda.springcloud.usuarios.models.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
