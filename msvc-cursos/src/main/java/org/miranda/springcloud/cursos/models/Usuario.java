package org.miranda.springcloud.cursos.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Usuario {

    private Long id;

    private String nombre;

    private String email;

    private String password;
}
