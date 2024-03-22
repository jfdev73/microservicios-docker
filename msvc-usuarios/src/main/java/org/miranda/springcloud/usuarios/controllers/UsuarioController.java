package org.miranda.springcloud.usuarios.controllers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.miranda.springcloud.usuarios.models.Usuario;
import org.miranda.springcloud.usuarios.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAll (){
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")

    public ResponseEntity<?>  findById(@PathVariable Long id){
        return ResponseEntity.ok( usuarioService.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Uset not found")));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Usuario u, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return valid(bindingResult);
        }
        Usuario user = usuarioService.save(u);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Usuario u,BindingResult bindingResult, @PathVariable Long id){
        if(bindingResult.hasErrors()){
            return valid(bindingResult);
        }
        Usuario user = usuarioService.update(u, id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  delete(@PathVariable Long id){
       usuarioService.deleteById(id);
       return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> getByCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(usuarioService.listarByIds(ids));

    }

    private static ResponseEntity<Map<String, String>> valid(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err->{
            errors.put(err.getField(), "El campo "+err.getField()+" "+err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
