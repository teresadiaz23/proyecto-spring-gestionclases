package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.SituacionExcepcionalServicio;
import com.salesianostriana.dam.gestionclases.service.SolicitudAmpliacionMatriculaServicio;
import com.salesianostriana.dam.gestionclases.service.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/profesor/")
@RequiredArgsConstructor
public class ProfesorController {

    private final UsuarioServicio usuarioServicio;
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
    public String administrarProfesor(Model model){
        model.addAttribute("profesores", usuarioServicio.findAllProfesores());
        return "administrador/profesor";
    }

    @GetMapping("/new")
    public String showProfesorForm(Model model){
        model.addAttribute("profesor", new Profesor());
        return "administrador/profesor_form";
    }

    @PostMapping("/submit")
    public String submitNewProfesor(@ModelAttribute("profesor") Profesor profesor){
        usuarioServicio.enviarCodigoUsuario(profesor);
        usuarioServicio.save(profesor);

        return "redirect:/admin/profesor/";
    }

    @GetMapping("/edit/{id}")
    public String editProfesor(@PathVariable("id") Long id, Model model){
        Usuario profesor = usuarioServicio.findById(id).get();
        if(profesor != null){
            model.addAttribute("profesor", profesor);
            return "administrador/profesor_form";
        }
        else{
            return "redirect:/admin/profesor/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarProfesorSubmit(@ModelAttribute("profesor") Profesor profesor){
        usuarioServicio.edit(profesor);
        return "redirect:/admin/profesor/";

    }

    @GetMapping("/borrar/{id}")
    public String borrarProfesor(@PathVariable("id") Long id, Model model){
        Usuario profesor = usuarioServicio.findById(id).get();
        if(profesor != null){
            usuarioServicio.delete(profesor);
        }
        return "redirect:/admin/profesor/";
    }
}
