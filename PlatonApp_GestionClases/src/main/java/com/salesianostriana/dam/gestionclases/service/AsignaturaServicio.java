package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.repository.AsignaturaRepository;
import com.salesianostriana.dam.gestionclases.repository.UsuarioRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignaturaServicio extends ServicioBaseImpl<Asignatura, Long, AsignaturaRepository> {


    public List<Asignatura> buscarAsignaturaPorCurso(Curso curso){
        return repositorio.buscarAsignaturasPorCurso(curso);
    }


    public List<Asignatura> asignaturasActivas(){
        return this.findAll().stream().filter(a -> a.isActivo()).collect(Collectors.toUnmodifiableList());
    }

    public Asignatura buscarPorNombre(String nombre, String nombre2){
        return repositorio.findByName(nombre, nombre2);
    }

    public SituacionExcepcional buscarSituacionExcepcional(Asignatura asignatura, Alumno alumno){
        return repositorio.situacionExcepcionalByAsigAlum(asignatura,alumno);
    }

    public SolicitudAmpliacionMatricula buscarAmpliacionMatricula(Asignatura asignatura, Alumno alumno){
        return repositorio.ampliacionMatriculaByAsigAlum(asignatura, alumno);
    }


}
