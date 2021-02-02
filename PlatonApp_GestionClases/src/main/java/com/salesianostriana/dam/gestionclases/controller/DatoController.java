package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.Dato;
import com.salesianostriana.dam.gestionclases.model.EstadoSolicitud;
import com.salesianostriana.dam.gestionclases.model.SituacionExcepcional;
import com.salesianostriana.dam.gestionclases.model.SolicitudAmpliacionMatricula;
import com.salesianostriana.dam.gestionclases.service.EscuelaServicio;
import com.salesianostriana.dam.gestionclases.service.SituacionExcepcionalServicio;
import com.salesianostriana.dam.gestionclases.service.SolicitudAmpliacionMatriculaServicio;
import com.salesianostriana.dam.gestionclases.service.TituloServicio;
import com.salesianostriana.dam.gestionclases.upload.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DatoController {

    private final StorageService storageService;
    private final EscuelaServicio escuelaServicio;
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

    @GetMapping("/escuela/")
    public String escuela(Model model) {

        return "/administrador/escuela";
    }

    @GetMapping("/datos/new")
    public String nuevosDatosForm(Model model) {
        model.addAttribute("datos", new Dato());

        return "/administrador/datos_form";
    }

    @PostMapping("/datos/new/submit")
    public String nuevosDatosSubmit(@RequestParam("file") MultipartFile file, @ModelAttribute("datos") Dato dato) {

        if (!file.isEmpty()) {
            String datos = storageService.store(file, dato.getNombre());
            dato.setRuta(MvcUriComponentsBuilder
                    .fromMethodName(DatoController.class, "serveFile", datos).build().toUriString());
        }
        else {

        }

        escuelaServicio.cargarEstructuraEscuela2(dato.getNombre());

        return "redirect:/admin/subida/success";

    }

    @GetMapping("/datos/horario/new")
    public String nuevosHorariosForm(Model model) {

        model.addAttribute("horario", new Dato());
        return "/administrador/datos_horario_form";
    }

    @PostMapping("/datos/horario/new/submit")
    public String nuevosDatosHorarioSubmit(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute("horario") Dato dato) {

        if (!file.isEmpty()) {
            String datos = storageService.store(file, dato.getNombre());
            dato.setRuta(MvcUriComponentsBuilder
                    .fromMethodName(DatoController.class, "serveFile", datos).build().toUriString());
        }
        else {

        }

        escuelaServicio.cargarHorarios(dato.getNombre());

        return "redirect:/admin/subida/success";

    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }
}
