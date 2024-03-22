package org.miranda.springcloud.usuarios.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Entity
@Table(name="us-uarios")
@Getter
@Setter
@ToString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;


}
