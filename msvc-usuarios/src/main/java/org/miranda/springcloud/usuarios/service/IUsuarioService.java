package org.miranda.springcloud.usuarios.service;

import org.miranda.springcloud.usuarios.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> findAll ();

    Optional<Usuario> findById(Long id);

    Usuario update(Usuario usuario, Long id);

    Usuario save (Usuario usuario);

    void deleteById(Long id);

    List<Usuario> listarByIds(Iterable<Long> ids);

    Optional<Usuario> findByEmail(String email);
}
