package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.EstadoSolicitud;
import com.salesianostriana.dam.gestionclases.model.SituacionExcepcional;
import com.salesianostriana.dam.gestionclases.model.SolicitudAmpliacionMatricula;
import com.salesianostriana.dam.gestionclases.service.SituacionExcepcionalServicio;
import com.salesianostriana.dam.gestionclases.service.SolicitudAmpliacionMatriculaServicio;
import com.salesianostriana.dam.gestionclases.service.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class JefeEstudiosController {

    private final SituacionExcepcionalServicio excepcionalServicio;
    private final SolicitudAmpliacionMatriculaServicio ampliacionServicio;
    private final UsuarioServicio usuarioServicio;


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
    public String index(){
        return "administrador/index";
    }

    @GetMapping("/subida/success")
    public String subidaCorrecta(){
        return "/administrador/subida_correcta";
    }

    @GetMapping("/carnet/")
    public String imprimirCarnet(Model model){
        model.addAttribute("alumnos", usuarioServicio.findAllAlumnos());
        model.addAttribute("usuarioServicio", usuarioServicio);
        return "/administrador/carnet";
    }




}
