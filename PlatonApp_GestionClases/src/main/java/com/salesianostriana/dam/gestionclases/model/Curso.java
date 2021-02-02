package com.salesianostriana.dam.gestionclases.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data  @NoArgsConstructor
@Builder @AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue
    private long id;

    private String nombre;
    private boolean activo = true;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Alumno> alumnos = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "curso", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Asignatura> asignaturas = new HashSet<>();

    @ManyToOne
    private Titulo titulo;

    public Curso(String nombre) {
        this.nombre = nombre;
    }

    public Curso(String nombre, Titulo titulo) {
        this.nombre = nombre;
        this.titulo = titulo;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    //Métodos helpers
    public void addAlumno(Alumno a){
        this.alumnos.add(a);
        a.setCurso(this);
    }
    public void removeAlumno(Alumno a){
        this.alumnos.remove(a);
        a.setCurso(null);
    }

    public void addAsignatura(Asignatura a){
        this.asignaturas.add(a);
        a.setCurso(this);
    }
    public void removeAsignatura(Asignatura a){
        this.asignaturas.remove(a);
        a.setCurso(null);
    }


}
