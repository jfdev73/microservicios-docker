package org.miranda.springcloud.cursos.models.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cursos_usuarios")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "usuario_id",unique = true)
    private Long usuarioId;

}
