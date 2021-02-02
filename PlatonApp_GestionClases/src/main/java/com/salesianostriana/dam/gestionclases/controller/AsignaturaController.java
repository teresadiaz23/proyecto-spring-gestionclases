package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/asignatura/")
@RequiredArgsConstructor
public class AsignaturaController {

    private final AsignaturaServicio asignaturaServicio;
    private final CursoServicio cursoServicio;
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
    public String administrarAsignatura(Model model){

        model.addAttribute("asignaturas", asignaturaServicio.asignaturasActivas());

        return "administrador/asignatura";
    }



    @GetMapping("/new")
    public String showAsignaturaForm(Model model){
        model.addAttribute("asignatura", new Asignatura());
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "administrador/asignatura_form";
    }

    @PostMapping("/submit")
    public String submitNewAsignatura(@ModelAttribute("asignatura") Asignatura asignatura, Model model){

        asignaturaServicio.save(asignatura);

        return "redirect:/admin/asignatura/";
    }

    @GetMapping("/edit/{id}")
    public String editAsignatura(@PathVariable("id") Long id, Model model){
        Asignatura asignatura = asignaturaServicio.findById(id).get();
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        if(asignatura != null){
            model.addAttribute("asignatura", asignatura);
            return "administrador/asignatura_form";
        }
        else{
            return "redirect:/admin/asignatura/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarAsignaturaSubmit(@ModelAttribute("asignatura") Asignatura asignatura, Model model){

        asignaturaServicio.edit(asignatura);

        return "redirect:/admin/asignatura/";

    }

    @GetMapping("/borrar/{id}")
    public String borrarAsignatura(@PathVariable("id") Long id, Model model){
        Asignatura asignatura = asignaturaServicio.findById(id).get();

        if(asignatura != null){
            asignatura.setActivo(false);
            for (Horario h: asignatura.getHorarios()){
                h.setActivo(false);
            }
            asignaturaServicio.edit(asignatura);

        }
        return "redirect:/admin/asignatura/";
    }
}
