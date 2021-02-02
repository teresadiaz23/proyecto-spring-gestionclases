package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.repository.SituacionExcepcionalRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SituacionExcepcionalServicio
        extends ServicioBaseImpl<SituacionExcepcional, Long, SituacionExcepcionalRepository> {



    public SituacionExcepcional buscarPorId(ExcepcionPK pk) {
        SituacionExcepcional situacionExcepcional = new SituacionExcepcional();

        for (SituacionExcepcional s: repositorio.findAll()){
            if (s.getId() == pk){
                situacionExcepcional = s;
            }
        }
        return situacionExcepcional;
    }

    public SituacionExcepcional buscarPorAlumnoAsignatura(Usuario alumno, Asignatura asignatura) {
        SituacionExcepcional situacionExcepcional = new SituacionExcepcional();

        for (SituacionExcepcional s: repositorio.findAll()){
            if (s.getId().getAlumno_id() == alumno.getId() && s.getId().getAsignatura_id() == asignatura.getId()){
                situacionExcepcional = s;
            }
        }
        return situacionExcepcional;
    }

}
