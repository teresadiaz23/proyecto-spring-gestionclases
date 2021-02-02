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
@RequestMapping("/admin/curso/asignatura/")
@RequiredArgsConstructor
public class AsignaturaCursoController {

    private final AsignaturaServicio asignaturaServicio;
    private final CursoServicio cursoServicio;
    private final HorarioServicio horarioServicio;
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


    @GetMapping("/{id}")
    public String administrarAsignatura(Model model, @PathVariable("id") Long id){

        Curso curso = cursoServicio.findById(id).get();
        List<Asignatura> asignaturas = asignaturaServicio.buscarAsignaturaPorCurso(curso).stream()
                .filter(a->a.isActivo()==true).collect(Collectors.toUnmodifiableList());
        model.addAttribute("asignaturas", asignaturas);
        model.addAttribute("curso", curso);

        return "administrador/asignatura_curso";
    }

    @GetMapping("/new")
    public String showAsignaturaForm(Model model){
        model.addAttribute("asignatura", new Asignatura());
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "administrador/asignatura_form";
    }

    @PostMapping("/submit")
    public String submitNewAsignatura(@ModelAttribute("asignatura") Asignatura asignatura, Model model){
        Long id = asignatura.getCurso().getId();
        asignaturaServicio.save(asignatura);

        return "redirect:/admin/curso/asignatura/"+id;
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
            return "redirect:/admin/curso/asignatura/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarAsignaturaSubmit(@ModelAttribute("asignatura") Asignatura asignatura, Model model){
        Long id = asignatura.getCurso().getId();
        asignaturaServicio.edit(asignatura);

        return "redirect:/admin/curso/asignatura/" + id;

    }

    @GetMapping("/borrar/{id}")
    public String borrarAsignatura(@PathVariable("id") Long id, Model model){
        Asignatura asignatura = asignaturaServicio.findById(id).get();
        Long idCurso = asignatura.getCurso().getId();

        if(asignatura != null){
            asignatura.setActivo(false);
            for (Horario h: asignatura.getHorarios()){
                h.setActivo(false);
            }
            asignaturaServicio.edit(asignatura);
        }

        return "redirect:/admin/curso/asignatura/"+idCurso;
    }

    @GetMapping("/horario/new/{id}")
    public String showHorarioForm(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();
        model.addAttribute("horario", new Horario());
        model.addAttribute("curso", curso);

        return "administrador/horario_asignatura_form";
    }

    @PostMapping("/horario/submit")
    public String submitNewHorario(@ModelAttribute("horario") Horario horario, Model model){

        Long idCurso = horario.getAsignatura().getCurso().getId();

        if(horarioServicio.agregarHorario(horario.getAsignatura(),horario)){
            horarioServicio.save(horario);
            return "redirect:/admin/curso/asignatura/"+ idCurso;
        }
        else{
            return "redirect:/admin/curso/asignatura/horario/new/"+idCurso;
        }



    }
}
