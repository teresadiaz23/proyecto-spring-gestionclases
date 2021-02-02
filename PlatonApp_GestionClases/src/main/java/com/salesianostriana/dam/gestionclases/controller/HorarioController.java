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
@RequestMapping("/admin/horario/")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioServicio horarioServicio;
    private final CursoServicio cursoServicio;
    private final AsignaturaServicio asignaturaServicio;
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
    public String administrarHorarios(Model model){
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "/administrador/horario";

    }

    @GetMapping("/mostrar/{id}")
    public String mostrarHorario(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();

        model.addAttribute("curso", curso);
        model.addAttribute("horarios", horarioServicio.horarioPorTramos(curso));

        return "/administrador/horario_mostrar";
    }

    @GetMapping("/new")
    public String showHorarioForm(Model model){
        model.addAttribute("horario", new Horario());
        model.addAttribute("asignaturas", asignaturaServicio.asignaturasActivas());
        return "administrador/horario_form";
    }

    @PostMapping("/submit")
    public String submitNewHorario(@ModelAttribute("horario") Horario horario, Model model){

        if(horarioServicio.agregarHorario(horario.getAsignatura(),horario)){
            horarioServicio.save(horario);
            return "redirect:/admin/asignatura/";
        }
        else{
            return "redirect:/admin/horario/new";
        }


    }

    @GetMapping("/edit/{id}")
    public String editHorario(@PathVariable("id") Long id, Model model){
        Horario horario = horarioServicio.findById(id).get();
        model.addAttribute("asignaturas", asignaturaServicio.asignaturasActivas());
        if(horario != null){
            model.addAttribute("horario", horario);
            return "administrador/horario_form";
        }
        else{
            return "redirect:/admin/horario/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarHorarioSubmit(@ModelAttribute("horario") Horario horario, Model model){

        horarioServicio.edit(horario);

        return "redirect:/admin/horario/";

    }

    @GetMapping("/borrar/{id}")
    public String borrarHorario(@PathVariable("id") Long id, Model model){
        Horario horario = horarioServicio.findById(id).get();

        if(horario != null){
            horarioServicio.delete(horario);
        }
        return "redirect:/admin/horario/";
    }



    
}
