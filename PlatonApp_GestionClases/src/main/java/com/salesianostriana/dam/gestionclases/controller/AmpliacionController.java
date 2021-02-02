package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/ampliacion/")
@RequiredArgsConstructor
public class AmpliacionController {

    private final SolicitudAmpliacionMatriculaServicio ampliacionServicio;
    private final UsuarioServicio usuarioServicio;
    private final AsignaturaServicio asignaturaServicio;
    private final HorarioServicio horarioServicio;
    private final SituacionExcepcionalServicio excepcionalServicio;

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
    public String mostrarAmpliacionesPendientes(Model model){
        model.addAttribute("ampliaciones", ampliacionServicio.findAll()
                .stream().filter(s -> s.getEstadoSolicitud()== EstadoSolicitud.PENDIENTE)
                .collect(Collectors.toUnmodifiableList()));
        model.addAttribute("ampliacionesRes", ampliacionServicio.findAll()
                .stream().filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.CONFIRMADA
                        || s.getEstadoSolicitud() == EstadoSolicitud.DENEGADA)
                .collect(Collectors.toUnmodifiableList()));
        return "/administrador/ampliaciones_pendientes";
    }

    @GetMapping("/confirmar/{id1}/{id2}")
    public String modificarSolicitud(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2, Model model){

        Usuario alumno = usuarioServicio.findById(id1).get();
        Asignatura asignatura = asignaturaServicio.findById(id2).get();
        SolicitudAmpliacionMatricula ampliacionMatricula = ampliacionServicio.buscarPorAlumnoAsignatura(alumno, asignatura);

        model.addAttribute("ampliacion", ampliacionMatricula);
        model.addAttribute("alumno", alumno);
        model.addAttribute("asignatura", asignatura);
        model.addAttribute("curso", asignatura.getCurso());
        model.addAttribute("horario2", horarioServicio.horarioPorTramos(asignatura.getCurso()));
        model.addAttribute("horarioAlumno", usuarioServicio.horarioPorTramosAlumno((Alumno)alumno));
        return "/administrador/ampliacion_modificar";

    }

    @PostMapping("/submit/")
    public String modificarAmpliacionSubmit(@ModelAttribute("ampliacion") SolicitudAmpliacionMatricula ampliacionMatricula){

        ampliacionMatricula.setFechaResolucion(LocalDate.now());
        ampliacionServicio.edit(ampliacionMatricula);

        return "redirect:/admin/ampliacion/";
    }
}
