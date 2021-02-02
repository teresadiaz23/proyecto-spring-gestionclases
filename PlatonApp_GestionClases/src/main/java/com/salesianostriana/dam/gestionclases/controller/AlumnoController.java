package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.email.EmailService;
import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.*;
import com.salesianostriana.dam.gestionclases.upload.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/alumno/")
@RequiredArgsConstructor
public class AlumnoController {

    private final UsuarioServicio usuarioServicio;
    private final EmailService emailService;
    private final StorageService storageService;
    private final EscuelaServicio escuelaServicio;
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

    @ModelAttribute("alumnos")
    public List<Usuario> alumnosOrdenados(Model model){
        return usuarioServicio.findAllAlumnos().stream()
                .sorted(Comparator.comparing(Usuario::getApellidos)).collect(Collectors.toUnmodifiableList());

    }

    @GetMapping("/")
    public String administrarAlumnos(Model model){

        return "administrador/alumno";
    }

    @GetMapping("/new")
    public String showAlumnoForm(Model model){
        model.addAttribute("alumno", new Alumno());
        model.addAttribute("cursos", cursoServicio.cursosActivos());
        return "administrador/alumno_form";
    }

    @PostMapping("/submit")
    public String submitNewAlumno(@ModelAttribute("alumno") Alumno alumno){
        usuarioServicio.enviarCodigoUsuario(alumno);
        usuarioServicio.save(alumno);

        return "redirect:/admin/alumno/";
    }

    @GetMapping("/edit/{id}")
    public String editAlumno(@PathVariable("id") Long id, Model model){
        Usuario alumno = usuarioServicio.findById(id).get();
        if(alumno != null){
            model.addAttribute("alumno", alumno);
            model.addAttribute("cursos", cursoServicio.cursosActivos());
            return "administrador/alumno_form";
        }
        else{
            return "redirect:/admin/alumno/new";
        }

    }

    @PostMapping("/edit/submit")
    public String editarAlumnoSubmit(@ModelAttribute("alumno") Alumno alumno){
        usuarioServicio.edit(alumno);

        return "redirect:/admin/alumno/";

    }

    @GetMapping("/borrar/{id}")
    public String borrarAlumno(@PathVariable("id") Long id, Model model){
        Usuario alumno = usuarioServicio.findById(id).get();
        if(alumno != null){
            usuarioServicio.delete(alumno);
        }
        return "redirect:/admin/alumno/";
    }

    @GetMapping("/datos/new")
    public String nuevosDatosForm(Model model) {
        model.addAttribute("datosAlumnos", new Dato());
        return "/administrador/datos_alumno_form";
    }

    @PostMapping("/datos/new/submit")
    public String nuevosHorarioSubmit(@RequestParam("file") MultipartFile file, @ModelAttribute("datosAlumnos") Dato dato) {

        if (!file.isEmpty()) {
            String datos = storageService.store(file, dato.getNombre());
            dato.setRuta(MvcUriComponentsBuilder
                    .fromMethodName(DatoController.class, "serveFile", datos).build().toUriString());
        }
        else {

        }

        escuelaServicio.cargarAlumnos(dato.getNombre());

        return "redirect:/admin/subida/success";

    }

    @GetMapping("/detalle/{id}")
    public String alumnosCurso(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();
        List<Asignatura> asignaturas = asignaturaServicio.buscarAsignaturaPorCurso(curso);


        model.addAttribute("curso", curso);
        model.addAttribute("asignaturas", asignaturas);
        model.addAttribute("alumnosCurso", usuarioServicio.findAlumnosByCurso(curso));
        model.addAttribute("usuarioServicio", usuarioServicio);
        model.addAttribute("asignaturaServicio", asignaturaServicio);

        return "/administrador/alumno_curso";
    }

    @GetMapping("/situacion/aprobado/new/{id}")
    public String newAsignaturaAprobadaCursoAnterior(@PathVariable("id") Long id, Model model){
        Curso curso = cursoServicio.findById(id).get();

        model.addAttribute("alumnos", usuarioServicio.findAlumnosByCurso(curso));
        model.addAttribute("curso", curso);
        model.addAttribute("asignaturas", asignaturaServicio.buscarAsignaturaPorCurso(curso).stream()
        .filter(Asignatura::isActivo).collect(Collectors.toUnmodifiableList()));
        model.addAttribute("aprobado", new Aprobado());
        return "/administrador/aprobado_curso_anterior";
    }

    @PostMapping("/situacion/aprobado/submit/")
    public String newAsignaturaAprobadaCursoAnteriorSubmit(@ModelAttribute("aprobado") Aprobado aprobado){

        Asignatura asignatura = asignaturaServicio.findById(aprobado.getAsignatura().getId()).get();
        Alumno alumno = (Alumno)aprobado.getAlumno();
        alumno.getAprobadas().add(asignatura);
        Long id = alumno.getCurso().getId();
        usuarioServicio.edit(alumno);

        return "redirect:/admin/alumno/detalle/" + id;
    }
}
