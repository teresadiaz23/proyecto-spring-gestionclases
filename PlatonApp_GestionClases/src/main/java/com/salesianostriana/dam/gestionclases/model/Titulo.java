package com.salesianostriana.dam.gestionclases.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor
@Builder @AllArgsConstructor
public class Titulo {

    @Id
    @GeneratedValue
    private long id;

    private String nombre;
    private boolean activo = true;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "titulo")
    private List<Curso> cursos = new ArrayList<>();

    public Titulo(String nombre) {
        this.nombre = nombre;

    }

    //Métodos helpers
    public void addCurso(Curso c){
        this.cursos.add(c);
        c.setTitulo(this);

    }
    public void removeCurso(Curso c){
        this.cursos.remove(c);
        c.setTitulo(null);
    }
}
