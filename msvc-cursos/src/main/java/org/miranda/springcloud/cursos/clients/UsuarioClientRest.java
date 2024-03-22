package org.miranda.springcloud.cursos.clients;

import org.miranda.springcloud.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name= "msvc-usuarios", url = "localhost:8001")

public interface UsuarioClientRest {
    @GetMapping("/users/{id}")
     Usuario detalle(@PathVariable Long id);

    @PostMapping("/users")
     Usuario crear (@RequestBody Usuario usuario);

    @GetMapping("/users/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
