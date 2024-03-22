package org.miranda.springcloud.cursos.service;

import lombok.RequiredArgsConstructor;
import org.miranda.springcloud.cursos.clients.UsuarioClientRest;
import org.miranda.springcloud.cursos.models.Usuario;
import org.miranda.springcloud.cursos.models.entity.Curso;
import org.miranda.springcloud.cursos.models.entity.CursoUsuario;
import org.miranda.springcloud.cursos.repository.CursoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CursoServiceImpl implements CursoService{

    private final CursoRepository repository;

    private final UsuarioClientRest clientRest;

    @Transactional(readOnly = true)
    @Override
    public List<Curso> findAll() {
        return (List<Curso>)repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> findByIdConUsuarios(Long id) {
        System.out.println("Ejecutando..");
        Optional<Curso> cursoOptional = repository.findById(id);
        if (cursoOptional.isPresent()){
            Curso curso = cursoOptional.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId)
                        .toList();

                List<Usuario> usuarios = clientRest.obtenerAlumnosPorCurso(ids);
                System.out.println("usuarios = " + usuarios);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }
    @Transactional
    @Override
    public Curso save(Curso curso) {
        return repository.save(curso);
    }
    @Override
    @Transactional
    public Curso update(Curso Curso, Long id) {
        Curso cursoUpdate = findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        cursoUpdate.setNombre(Curso.getNombre());

        return repository.save(cursoUpdate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Curso> curso = findById(id);
        if(curso.isPresent()) {
            repository.deleteById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found");

        }
    }

    @Override
    @Transactional
    public void deleteCursoUsuarioById(Long id) {

        repository.deleteCursoUsuarioById(id);

    }


    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long id) {
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){
            Usuario usuarioMsvc = clientRest.detalle(usuario.getId());

            Curso curso = o.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);

        System.out.println("Curso = " + o.get());
        if(o.isPresent()){
            Usuario usuarioNuevoMsvc = clientRest.crear(usuario);
            System.out.println("usuarioNuevoMsvc = " + usuarioNuevoMsvc);

            Curso curso = o.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();

    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc = clientRest.detalle(usuario.getId());
            System.out.println("usuarioNuevoMsvc = " + usuarioMsvc);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }
}
