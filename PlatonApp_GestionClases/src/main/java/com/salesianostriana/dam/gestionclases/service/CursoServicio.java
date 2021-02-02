package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.repository.CursoRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoServicio extends ServicioBaseImpl<Curso, Long, CursoRepository> {


    public List<Curso> cursosActivos(){
        return this.findAll().stream().filter(c -> c.isActivo()).collect(Collectors.toUnmodifiableList());
    }

    public Curso buscarPorNombre(String nombre){
        return repositorio.findByName(nombre);
    }

    public Curso buscarCursoSegundo(Titulo titulo){
        return repositorio.cursoSegundo(titulo);
    }

    public boolean alumnosAmpliacion(Curso curso, Alumno alumno){
        boolean ampliacion = false;
        if(curso.getNombre().contains("1º")){
            if(!alumno.getAprobadas().isEmpty() || !alumno.getSituacionExcepcional().isEmpty()){
                for(SituacionExcepcional s: alumno.getSituacionExcepcional()){
                    if(s.getEstadoSolicitud() == EstadoSolicitud.CONFIRMADA){
                        ampliacion = true;
                    }
                }

            }

        }

        return ampliacion;
    }
}
