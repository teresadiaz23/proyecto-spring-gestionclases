package com.salesianostriana.dam.gestionclases.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor
@Builder @AllArgsConstructor
public class SituacionExcepcional {

    @EmbeddedId
    private ExcepcionPK id = new ExcepcionPK();

    private LocalDate fechaSolicitud = LocalDate.now();
    private TipoSituacionExpecional tipo;
    private String adjunto;
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

    public SituacionExcepcional(Alumno alumno, Asignatura asignatura) {
        this.alumno = alumno;
        this.asignatura = asignatura;
    }

    public SituacionExcepcional(TipoSituacionExpecional tipo, Alumno alumno, Asignatura asignatura) {
        this.tipo = tipo;
        this.alumno = alumno;
        this.asignatura = asignatura;
    }

    public SituacionExcepcional(TipoSituacionExpecional tipo, String adjunto, Alumno alumno, Asignatura asignatura) {
        this.tipo = tipo;
        this.adjunto = adjunto;
        this.alumno = alumno;
        this.asignatura = asignatura;
    }
}
