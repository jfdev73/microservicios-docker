package org.miranda.springcloud.cursos.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.miranda.springcloud.cursos.models.Usuario;
import org.miranda.springcloud.cursos.models.entity.Curso;
import org.miranda.springcloud.cursos.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequestMapping("/cursos")
@RequiredArgsConstructor
@RestController
public class CursoController {

    private final CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  findById(@PathVariable Long id){
       /* return ResponseEntity.ok( service.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Uset not found")));*/
        Optional<Curso> cursoOptional = service.findByIdConUsuarios(id);
        if (cursoOptional.isPresent()){
            return ResponseEntity.ok(cursoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Curso cursoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return valid(bindingResult);
        }
        Curso curso = service.save(cursoRequest);
        return new ResponseEntity<>(curso,HttpStatus.CREATED);


    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Curso cursoRequest, BindingResult bindingResult, @PathVariable Long id){
        if (bindingResult.hasErrors()){
            return valid(bindingResult);
        }
        Curso curso = service.update(cursoRequest, id);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.asignarUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por id, o error en la comunicacion: "+e.getMessage()));
        }

        if(o.isPresent()){
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.crearUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario o error en la comunicacion: "+e.getMessage()));
        }

        if(o.isPresent()){
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.eliminarUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por id, o error en la comunicacion: "+e.getMessage()));
        }

        if(o.isPresent()){
            return  ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioById(@PathVariable Long id){
        System.out.println("Eliminando...");
        service.deleteCursoUsuarioById(id);
        return ResponseEntity.noContent().build();

    }
    private static ResponseEntity<Map<String, String>> valid(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err-> errors.put(err.getField(), "El campo "+err.getField()+" "+err.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

}
