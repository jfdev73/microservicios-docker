package org.miranda.springcloud.cursos.service;

import org.miranda.springcloud.cursos.models.Usuario;
import org.miranda.springcloud.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> findAll();
    Optional<Curso> findById(Long id);
    Curso save (Curso curso);
    Curso update(Curso curso, Long id);
    void deleteById(Long id);

    void deleteCursoUsuarioById(Long id);

    Optional<Curso> findByIdConUsuarios(Long id);


    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}
