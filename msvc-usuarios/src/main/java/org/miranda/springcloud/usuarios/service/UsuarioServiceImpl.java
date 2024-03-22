package org.miranda.springcloud.usuarios.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.miranda.springcloud.usuarios.client.CursoClienteRest;
import org.miranda.springcloud.usuarios.models.Usuario;
import org.miranda.springcloud.usuarios.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService{

    private final UsuarioRepository repository;

    private final CursoClienteRest clienteRest;


    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>)repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        Optional<Usuario> user = findByEmail(usuario.getEmail());
        if(user.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con este correo");
        }
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario update(Usuario usuario, Long id) {
       Usuario userUpdate = findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!userUpdate.getEmail().equals(usuario.getEmail())) {
            Optional<Usuario> usuarioConNuevoEmail = findByEmail(usuario.getEmail());
            if (usuarioConNuevoEmail.isPresent()) {
                // Aquí puedes decidir cómo manejar la situación
                // por ejemplo, lanzar una excepción o fusionar los usuarios
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con el nuevo correo");
            }
        }

       userUpdate.setEmail(usuario.getEmail());
       userUpdate.setNombre(usuario.getNombre());
       userUpdate.setPassword(usuario.getPassword());
        return repository.save(userUpdate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Usuario> o = findById(id);
        if(o.isPresent()) {
            repository.deleteById(id);
            clienteRest.eliminarCursoUsuarioById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found");

        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> listarByIds(Iterable<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}

