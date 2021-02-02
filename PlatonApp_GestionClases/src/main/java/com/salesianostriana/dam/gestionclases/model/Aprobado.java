package com.salesianostriana.dam.gestionclases.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aprobado {


    private Usuario alumno;
    private Asignatura asignatura;
}
