package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.CursoServicio;
import com.salesianostriana.dam.gestionclases.service.SituacionExcepcionalServicio;
import com.salesianostriana.dam.gestionclases.service.SolicitudAmpliacionMatriculaServicio;
import com.salesianostriana.dam.gestionclases.service.TituloServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/curso/")
@RequiredArgsConstructor
public class CursoController {

    private final CursoServicio cursoServicio;
    private final TituloServicio tituloServicio;
    private final SituacionExcepcionalServicio excepcionalServicio;
    private final SolicitudAmpliacionMatriculaServicio ampliacionServicio;

    @ModelAttribute("solicitudesP")
    public List<SituacionExcepcional> listaSolicitudesPendientes() {
        return excepcionalServicio.findAll().stream().filter(s->s.getEstadoSolicitud()== EstadoSolicitud.PENDIENTE)
                .collect(Collectors.toUnmodifiableList());

    }

    @ModelAttribute("ampliacionesP")
    public List<SolicitudAmpliacionMatricula> listaAmpliacionesPendientes() {
        return ampliacionServicio.findAll().stream().filter(s->s.getEstadoSolicitud()== EstadoSolicitud.PENDIENTE)
                .collect(Collectors.toUnmodifiableList());

    }

    @GetMapping("/")
    public String administrarCurso(Model model){
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "administrador/curso";
    }

    @GetMapping("/new")
    public String showCursoForm(Model model){
        model.addAttribute("curso", new Curso());
        model.addAttribute("titulos", tituloServicio.titulosActivos());
        return "administrador/curso_form";
    }

    @PostMapping("/submit")
    public String submitNewCurso(@ModelAttribute("curso") Curso curso, Model model){
        cursoServicio.save(curso);

        return "redirect:/admin/curso/";
    }

    @GetMapping("/edit/{id}")
    public String editCurso(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();
        model.addAttribute("titulos", tituloServicio.titulosActivos());
        if(curso != null){
            model.addAttribute("curso", curso);
            return "administrador/curso_form";
        }
        else{
            return "redirect:/admin/curso/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarCursoSubmit(@ModelAttribute("curso") Curso curso, Model model){
        cursoServicio.edit(curso);

        return "redirect:/admin/curso/";

    }

    @GetMapping("/borrar/{id}")
    public String borrarCurso(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();
        if(curso != null){
            curso.setActivo(false);
            cursoServicio.edit(curso);
        }
        return "redirect:/admin/curso/";
    }
}
