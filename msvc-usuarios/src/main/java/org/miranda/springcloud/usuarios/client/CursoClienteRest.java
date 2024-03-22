package org.miranda.springcloud.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos", url = "localhost:8002")
public interface CursoClienteRest {

    @DeleteMapping("/cursos/eliminar-curso-usuario/{id}")
    void eliminarCursoUsuarioById(@PathVariable Long id);
}

