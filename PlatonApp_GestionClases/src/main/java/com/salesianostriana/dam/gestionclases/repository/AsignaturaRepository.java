package com.salesianostriana.dam.gestionclases.repository;

import com.salesianostriana.dam.gestionclases.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    @Query("select a from Asignatura a where a.curso = :curso")
    List<Asignatura> buscarAsignaturasPorCurso(@Param("curso") Curso curso);

    @Query("select a from Asignatura a where a.nombre = :asignatura and a.curso.nombre = :curso ")
    Asignatura findByName(@Param("asignatura") String asignatura, @Param("curso") String curso);


    @Query("select s from SituacionExcepcional s where s.asignatura = :asignatura and s.alumno = :alumno")
    SituacionExcepcional situacionExcepcionalByAsigAlum(@Param("asignatura") Asignatura asignatura,
                                                        @Param("alumno") Alumno alumno);

    @Query("select s from SolicitudAmpliacionMatricula s where s.asignatura = :asignatura and s.alumno = :alumno")
    SolicitudAmpliacionMatricula ampliacionMatriculaByAsigAlum(@Param("asignatura") Asignatura asignatura,
                                                        @Param("alumno") Alumno alumno);


}
