package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.Asignatura;
import com.salesianostriana.dam.gestionclases.model.SituacionExcepcional;
import com.salesianostriana.dam.gestionclases.model.SolicitudAmpliacionMatricula;
import com.salesianostriana.dam.gestionclases.model.Usuario;
import com.salesianostriana.dam.gestionclases.repository.SolicitudAmpliacionMatriculaRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import org.springframework.stereotype.Service;

@Service
public class SolicitudAmpliacionMatriculaServicio
        extends ServicioBaseImpl<SolicitudAmpliacionMatricula, Long, SolicitudAmpliacionMatriculaRepository> {


    public SolicitudAmpliacionMatricula buscarPorAlumnoAsignatura(Usuario alumno, Asignatura asignatura) {
        SolicitudAmpliacionMatricula ampliacionMatricula = new SolicitudAmpliacionMatricula();

        for (SolicitudAmpliacionMatricula s: repositorio.findAll()){
            if (s.getId().getAlumno_id() == alumno.getId() && s.getId().getAsignatura_id() == asignatura.getId()){
                ampliacionMatricula = s;
            }
        }
        return ampliacionMatricula;
    }
}
