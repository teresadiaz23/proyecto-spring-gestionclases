package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.Asignatura;
import com.salesianostriana.dam.gestionclases.model.Curso;
import com.salesianostriana.dam.gestionclases.model.Horario;
import com.salesianostriana.dam.gestionclases.repository.HorarioRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HorarioServicio extends ServicioBaseImpl<Horario, Long, HorarioRepository> {


    @Autowired
    private AsignaturaServicio asignaturaServicio;


    public boolean agregarHorario(Asignatura asignatura, Horario horario){
        boolean agregado = false;
        Curso curso = asignatura.getCurso();

        List<Horario> horario2 = new ArrayList<>();
        for (Asignatura a : curso.getAsignaturas()){
            for (Horario h: a.getHorarios()){
                if(!h.isActivo()){

                    horario2.add(h);
                }

            }
        }
        for(Horario h: horario2){
            if(h.getDia() == horario.getDia() && h.getTramoHorario() == horario.getTramoHorario()){
                agregado = true;
                this.delete(h);

            }
        }


        return agregado;

    }

    public List<Horario> obtenerHorario(Curso curso){
        List<Horario> horario = new ArrayList<>();
        List<Asignatura> asignaturas = curso.getAsignaturas().stream().collect(Collectors.toList());


        for(Asignatura a : asignaturas){
            for (Horario h : a.getHorarios()){
                horario.add(h);

            }
        }

        return horario;
    }

    public List<List<Horario>> horarioPorTramos(Curso curso){
        int tramo = 1;
        List<Horario> horario = obtenerHorario(curso);
        List<List<Horario>> listaHorarios = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            int finalTramo = tramo;

            listaHorarios.add(horario.stream()
                    .filter(h -> h.getTramoHorario()== finalTramo).sorted(Comparator.comparing(Horario::getDia))
                    .collect(Collectors.toUnmodifiableList()));

            tramo++;

        }

        return listaHorarios;
    }



    public List<List<Horario>> horarioPorTramos2(Curso curso){
        int tramo = 1;
        List<Horario> horario = obtenerHorario(curso);

        List<List<Horario>> listaHorarios = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            int finalTramo = tramo;

            listaHorarios.add(horario.stream()
                    .filter(h -> h.getTramoHorario()== finalTramo).sorted(Comparator.comparing(Horario::getDia))
                    .collect(Collectors.toList()));

            tramo++;

        }

        return listaHorarios;
    }
}
