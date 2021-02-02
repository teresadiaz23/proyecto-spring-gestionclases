package com.salesianostriana.dam.gestionclases.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor
@Builder @AllArgsConstructor
public class Asignatura {

    @Id @GeneratedValue
    private long id;
    private String nombre;
    private boolean activo = true;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "asignatura", fetch = FetchType.LAZY)
    private List<SituacionExcepcional> situacionExcepcional = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "asignatura", fetch = FetchType.LAZY)
    private List<SolicitudAmpliacionMatricula> solicitudAmpliacionMatricula = new ArrayList<>();

    @ManyToOne
    private Curso curso;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "aprobadas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Alumno> alumnos = new ArrayList<>();


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "asignatura", fetch =FetchType.EAGER)
    private List<Horario> horarios = new ArrayList<>();

    public Asignatura(String nombre) {
        this.nombre = nombre;
    }

    public Asignatura(String nombre, Curso curso) {
        this.nombre = nombre;
        this.curso = curso;
    }

    //Métodos helpers
    public void addHorario(Horario h){
        this.horarios.add(h);
        h.setAsignatura(this);
    }
    public void removeHorario(Horario h){
        this.horarios.add(h);
        h.setAsignatura(null);
    }

}
