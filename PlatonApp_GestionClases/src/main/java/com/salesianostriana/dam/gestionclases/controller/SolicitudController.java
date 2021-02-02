package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.AsignaturaServicio;
import com.salesianostriana.dam.gestionclases.service.SituacionExcepcionalServicio;
import com.salesianostriana.dam.gestionclases.service.SolicitudAmpliacionMatriculaServicio;
import com.salesianostriana.dam.gestionclases.service.UsuarioServicio;
import com.salesianostriana.dam.gestionclases.upload.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/solicitud/")
@RequiredArgsConstructor
public class SolicitudController {

    private final SituacionExcepcionalServicio excepcionalServicio;
    private final FileSystemStorageService fileSystem;
    private final UsuarioServicio usuarioServicio;
    private final AsignaturaServicio asignaturaServicio;
    private final SolicitudAmpliacionMatriculaServicio ampliacionServicio;

    @ModelAttribute("solicitudesP")
    public List<SituacionExcepcional> listaSolicitudes() {
        return excepcionalServicio.findAll().stream().filter(s->s.getEstadoSolicitud()== EstadoSolicitud.PENDIENTE)
                .collect(Collectors.toUnmodifiableList());

    }

    @ModelAttribute("ampliacionesP")
    public List<SolicitudAmpliacionMatricula> listaAmpliacionesPendientes() {
        return ampliacionServicio.findAll().stream().filter(s->s.getEstadoSolicitud()== EstadoSolicitud.PENDIENTE)
                .collect(Collectors.toUnmodifiableList());

    }

    @GetMapping("/")
    public String mostrarSolicitudesPendientes(Model model){
        model.addAttribute("solicitudes", excepcionalServicio.findAll()
                .stream().filter(s -> s.getEstadoSolicitud()== EstadoSolicitud.PENDIENTE)
                .collect(Collectors.toUnmodifiableList()));
        model.addAttribute("solicitudesRes", excepcionalServicio.findAll()
                .stream().filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.CONFIRMADA
                || s.getEstadoSolicitud() == EstadoSolicitud.DENEGADA)
                .collect(Collectors.toUnmodifiableList()));
        return "/administrador/solicitudes_pendientes";
    }

    @GetMapping("/archivo/{nombre}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable("nombre") String nombre){
        Resource resource = fileSystem.loadAsResource(nombre);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename()+"\"").body(resource);
    }

    @GetMapping("/confirmar/{id1}/{id2}")
    public String modificarSolicitud(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2, Model model){

        Usuario alumno = usuarioServicio.findById(id1).get();
        Asignatura asignatura = asignaturaServicio.findById(id2).get();
        SituacionExcepcional situacionExcepcional = excepcionalServicio.buscarPorAlumnoAsignatura(alumno,asignatura);

        model.addAttribute("solicitud", situacionExcepcional);
        model.addAttribute("alumno", alumno);
        model.addAttribute("asignatura", asignatura);
        model.addAttribute("curso", asignatura.getCurso());
        return "/administrador/solicitud_modificar";

    }


    @PostMapping("/submit/")
    public String modificarSolicitudSubmit(@ModelAttribute("solicitud") SituacionExcepcional situacionExcepcional){

        situacionExcepcional.setFechaResolucion(LocalDate.now());

        excepcionalServicio.edit(situacionExcepcional);

        return "redirect:/admin/solicitud/";
    }

    
}
