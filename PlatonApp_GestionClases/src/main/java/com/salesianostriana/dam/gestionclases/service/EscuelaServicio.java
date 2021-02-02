package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EscuelaServicio {

    @Autowired
    private CursoServicio cursoServicio;

    @Autowired
    private TituloServicio tituloServicio;

    @Autowired
    private AsignaturaServicio asignaturaServicio;

    @Autowired
    private HorarioServicio horarioServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    public void cargarEstructuraEscuela(String nombre){
        String path = "./upload-dir/" + nombre + ".csv";
        Set<Curso> cursos = new HashSet<>();
        Set<Titulo> titulos = new HashSet<>();
        Set<Asignatura> asignaturas = new HashSet<>();

        try{
            titulos = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Titulo(values[0]);
            }).collect(Collectors.toUnmodifiableSet());
            for(Titulo t: titulos){
                tituloServicio.save(t);
            }

            cursos = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Curso(values[1],tituloServicio.buscarPorNombre(values[0]));

            }).collect(Collectors.toUnmodifiableSet());
            for (Curso c: cursos){

                cursoServicio.save(c);
            }

            asignaturas = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Asignatura(values[2], cursoServicio.buscarPorNombre(values[1]));

            }).collect(Collectors.toUnmodifiableSet());
            for (Asignatura a: asignaturas){

                asignaturaServicio.save(a);
            }

        } catch (Exception e){
            System.err.println("Error de lectura");
            System.exit(-1);
        }
    }

    public void cargarHorarios(String nombre){
        String path = "./upload-dir/" + nombre + ".csv";
        List<Horario> horarios = new ArrayList<>();
        try{

            horarios = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Horario(Dia.valueOf(values[2]),Integer.parseInt(values[3]), asignaturaServicio.buscarPorNombre(values[1], values[0]));

            }).collect(Collectors.toUnmodifiableList());
            for (Horario h : horarios){
                horarioServicio.save(h);
            }

        } catch (Exception e){
            System.err.println("Error de lectura");
            System.exit(-1);
        }


    }

    public void cargarAlumnos(String nombre){
        String path = "./upload-dir/" + nombre + ".csv";
        Set<Alumno> alumnos = new HashSet<>();
        try{

            alumnos = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Alumno(values[0], values[1], values[2], LocalDate.parse(values[3],
                        DateTimeFormatter.ofPattern("d/M/u")), cursoServicio.buscarPorNombre(values[4]));

            }).collect(Collectors.toUnmodifiableSet());


            for(Alumno a : alumnos){

                usuarioServicio.save(a);
            }



        } catch (Exception e){
            System.err.println("Error de lectura");
            System.exit(-1);
        }


    }

    public void cargarEstructuraEscuela2(String nombre){
        String path = "./upload-dir/" + nombre + ".csv";
        Set<Curso> cursos = new HashSet<>();
        Set<Titulo> titulos = new HashSet<>();
        Set<Asignatura> asignaturas = new HashSet<>();
        List<Horario> horarios = new ArrayList<>();

        try{
            titulos = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Titulo(values[0]);
            }).collect(Collectors.toUnmodifiableSet());
            for(Titulo t: titulos){
                tituloServicio.save(t);
            }

            cursos = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Curso(values[1],tituloServicio.buscarPorNombre(values[0]));


            }).collect(Collectors.toUnmodifiableSet());
            for (Curso c: cursos){

                cursoServicio.save(c);
            }

            asignaturas = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Asignatura(values[2], cursoServicio.buscarPorNombre(values[1]));

            }).collect(Collectors.toUnmodifiableSet());
            for (Asignatura a: asignaturas){

                asignaturaServicio.save(a);
            }

            horarios = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String[] values = line.split(";");
                return new Horario(Dia.valueOf(values[3]),Integer.parseInt(values[4]), asignaturaServicio.buscarPorNombre(values[2], values[1]));

            }).collect(Collectors.toUnmodifiableList());
            for (Horario h : horarios){
                horarioServicio.save(h);
            }

        } catch (Exception e){
            System.err.println("Error de lectura");
            System.exit(-1);
        }
    }


}
