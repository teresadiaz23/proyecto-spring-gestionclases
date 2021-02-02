package com.salesianostriana.dam.gestionclases.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor
@Builder @AllArgsConstructor
public class SolicitudAmpliacionMatricula {

    @EmbeddedId
    private ExcepcionPK id = new ExcepcionPK();

    private LocalDate fechaSolicitud = LocalDate.now();
    private LocalDate fechaResolucion;
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE;

    @ManyToOne
    @MapsId("alumno_id")
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne
    @MapsId("asignatura_id")
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    public SolicitudAmpliacionMatricula(Alumno alumno, Asignatura asignatura) {
        this.alumno = alumno;
        this.asignatura = asignatura;
    }
}
