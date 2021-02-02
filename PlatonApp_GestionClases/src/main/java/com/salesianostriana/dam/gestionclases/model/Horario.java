package com.salesianostriana.dam.gestionclases.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data @NoArgsConstructor
@Builder @AllArgsConstructor
public class Horario{

    @Id @GeneratedValue
    private long id;
    private Dia dia;
    private int tramoHorario;
    private boolean activo = true;

    @ManyToOne
    private Asignatura asignatura;


    public Horario(Dia dia, int tramoHorario, Asignatura asignatura) {
        this.dia = dia;
        this.tramoHorario = tramoHorario;
        this.asignatura = asignatura;
    }

    public Horario(int tramoHorario, Asignatura asignatura) {
        this.tramoHorario = tramoHorario;
        this.asignatura = asignatura;
    }
}
