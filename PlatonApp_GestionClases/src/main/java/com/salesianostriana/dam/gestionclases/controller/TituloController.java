package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.EstadoSolicitud;
import com.salesianostriana.dam.gestionclases.model.SituacionExcepcional;
import com.salesianostriana.dam.gestionclases.model.SolicitudAmpliacionMatricula;
import com.salesianostriana.dam.gestionclases.model.Titulo;
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
@RequestMapping("/admin/titulo/")
@RequiredArgsConstructor
public class TituloController {

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
    public String administrarTitulos(Model model){
        model.addAttribute("titulos", tituloServicio.titulosActivos());
        return "administrador/titulo";
    }

    @GetMapping("/new")
    public String showTituloForm(Model model){
        model.addAttribute("titulito", new Titulo());
        return "administrador/titulo_form";
    }

    @PostMapping("/submit")
    public String submitNewTitulo(@ModelAttribute("titulito") Titulo titulo){
        tituloServicio.save(titulo);

        return "redirect:/admin/titulo/";
    }

    @GetMapping("/edit/{id}")
    public String editTitulo(@PathVariable("id") Long id, Model model){
        Titulo titulo = tituloServicio.findById(id).get();
        if(titulo != null){
            model.addAttribute("titulito", titulo);
            return "administrador/titulo_form";
        }
        else{
            return "redirect:/admin/titulo/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarTituloSubmit(@ModelAttribute("titulito") Titulo titulo){
        tituloServicio.edit(titulo);
        return "redirect:/admin/titulo/";

    }

    @GetMapping("/borrar/{id}")
    public String borrarTitulo(@PathVariable("id") Long id, Model model){
        Titulo titulo = tituloServicio.findById(id).get();
        if(titulo != null){
            titulo.setActivo(false);
            tituloServicio.edit(titulo);
        }
        return "redirect:/admin/titulo/";
    }



}
