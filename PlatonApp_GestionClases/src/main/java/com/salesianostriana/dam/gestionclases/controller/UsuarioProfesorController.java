package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.Asignatura;
import com.salesianostriana.dam.gestionclases.model.Curso;
import com.salesianostriana.dam.gestionclases.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teacher/")
@RequiredArgsConstructor
public class UsuarioProfesorController {

    private final UsuarioServicio usuarioServicio;
    private final TituloServicio tituloServicio;
    private final CursoServicio cursoServicio;
    private final AsignaturaServicio asignaturaServicio;
    private final HorarioServicio horarioServicio;

    @GetMapping("/")
    public String index(){
        return "/profesor/index";
    }

    @GetMapping("/titulo/")
    public String mostrarTitulos(Model model){
        model.addAttribute("titulos", tituloServicio.titulosActivos());
        return "/profesor/titulo";
    }

    @GetMapping("/curso/")
    public String mostrarCursos(Model model){
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "/profesor/curso";
    }

    @GetMapping("/asignatura/{id}")
    public String mostrarAsignaturas(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();
        model.addAttribute("asignaturas", curso.getAsignaturas());
        model.addAttribute("curso", curso);
        return "/profesor/asignatura";
    }


    @GetMapping("/alumno/")
    public String mostrarAlumnos(Model model){
        model.addAttribute("alumnos", usuarioServicio.findAllAlumnos());
        return "/profesor/alumno";
    }

    @GetMapping("/horario/")
    public String elegirHorario(Model model){
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "/profesor/horario";

    }


    @GetMapping("/horario/mostrar/{id}")
    public String mostrarHorario(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();

        model.addAttribute("curso", curso);
        model.addAttribute("horarios", horarioServicio.horarioPorTramos(curso));

        return "/profesor/horario_mostrar";
    }

    @GetMapping("/curso/detalle/{id}")
    public String alumnosCurso(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();
        List<Asignatura> asignaturas = asignaturaServicio.buscarAsignaturaPorCurso(curso);

        model.addAttribute("curso", curso);
        model.addAttribute("asignaturas", asignaturas);
        model.addAttribute("alumnosCurso", usuarioServicio.findAlumnosByCurso(curso));
        model.addAttribute("usuarioServicio", usuarioServicio);
        model.addAttribute("asignaturaServicio", asignaturaServicio);

        return "/profesor/alumno_curso";
    }

}
