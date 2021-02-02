package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.*;
import com.salesianostriana.dam.gestionclases.upload.FileSystemStorageService;
import com.salesianostriana.dam.gestionclases.upload.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/alum/")
@RequiredArgsConstructor
public class UsuarioAlumnoController {

    private final AsignaturaServicio asignaturaServicio;
    private final UsuarioServicio usuarioServicio;
    private final SituacionExcepcionalServicio excepcionalServicio;
    private final StorageService storageService;
    private final FileSystemStorageService fileSystem;
    private final CursoServicio cursoServicio;
    private final SolicitudAmpliacionMatriculaServicio ampliacionMatriculaServicio;
    private final HorarioServicio horarioServicio;

    @GetMapping("/")
    public String index(){
        return "alumno/index";
    }

    @GetMapping("/curso/")
    public String mostrarCurso(@AuthenticationPrincipal Alumno alumno, Model model){
        model.addAttribute("alumno", alumno);
        model.addAttribute("curso", alumno.getCurso());
        model.addAttribute("asignaturas", usuarioServicio.asignaturasMatriculadas(alumno));
        model.addAttribute("cursoServicio", cursoServicio);
         return "/alumno/mi_curso";

    }

    @GetMapping("/curso/situacion/new/")
    public String newSituacionExcepcional(@AuthenticationPrincipal Alumno alumno, Model model){

        model.addAttribute("alumno", alumno);
        model.addAttribute("curso", alumno.getCurso());
        model.addAttribute("asignaturas", usuarioServicio.asignaturasMatriculadas(alumno));
        model.addAttribute("situacion", new SituacionExcepcional());
        return "/alumno/curso_situacion";
    }

    @PostMapping("/curso/situacion/submit/")
    public String newSituacionExcepcionalSubmit(@AuthenticationPrincipal Alumno alumno,
                                                @ModelAttribute("situacion") SituacionExcepcional situacionExcepcional,
                                                @RequestParam("file") MultipartFile file){


        if (!file.isEmpty()) {
            String archivo = storageService.store(file, alumno.getApellidos() + "," + alumno.getNombre()+
                    "-" + situacionExcepcional.getAsignatura().getNombre());

            situacionExcepcional.setAdjunto(archivo);
        }

        situacionExcepcional.setAlumno(alumno);
        alumno.getSituacionExcepcional().add(situacionExcepcional);

        excepcionalServicio.save(situacionExcepcional);
        Long id = situacionExcepcional.getAsignatura().getId();

        return "redirect:/alum/curso/situacion/estado/" + id;
    }



    @GetMapping("/curso/situacion/estado/{id}")
    public String estadoSolicitudExepcional(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal Alumno alumno, Model model){
        Asignatura asignatura = asignaturaServicio.findById(id).get();
        model.addAttribute("alumno", alumno);
        model.addAttribute("curso", alumno.getCurso());
        model.addAttribute("asignatura", asignatura);
        model.addAttribute("situacion", asignaturaServicio.buscarSituacionExcepcional(asignatura, alumno));

        return "/alumno/curso_situacion_estado";

    }

    @GetMapping("/curso/solicitud/estado/")
    public String estadosSolicitudes(@AuthenticationPrincipal Alumno alumno, Model model){

        model.addAttribute("solicitudes", alumno.getSituacionExcepcional());
        model.addAttribute("ampliaciones", alumno.getSolicitudAmpliacionMatricula());
        return "/alumno/estado_solicitud";
    }

    @GetMapping("/archivo/{nombre}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable("nombre") String nombre){
        Resource resource = fileSystem.loadAsResource(nombre);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename()+"\"").body(resource);
    }

    @GetMapping("/horario/")
    public String mostrarHorario(@AuthenticationPrincipal Alumno alumno, Model model){
        Curso curso = alumno.getCurso();
        model.addAttribute("curso", curso);
        model.addAttribute("horarios", usuarioServicio.horarioPorTramosAlumno(alumno));
        model.addAttribute("usuarioServicio", usuarioServicio);
        return "/alumno/horario";

    }

    @GetMapping("/curso/ampliacion/new")
    public String newAmpliacion(@AuthenticationPrincipal Alumno alumno, Model model){
        model.addAttribute("alumno", alumno);
        model.addAttribute("curso", alumno.getCurso());
        model.addAttribute("ampliacion", new SolicitudAmpliacionMatricula());
        model.addAttribute("asignaturas", usuarioServicio.asignaturasSegundoCurso(alumno));
        model.addAttribute("horarios", horarioServicio.horarioPorTramos(cursoServicio
                .buscarCursoSegundo(alumno.getCurso().getTitulo())));

        return "/alumno/curso_ampliacion";
    }

    @PostMapping("/curso/ampliacion/submit")
    public String newAmpliacionSubmit(@AuthenticationPrincipal Alumno alumno,
                                      @ModelAttribute("ampliacion") SolicitudAmpliacionMatricula solicitudAmpliacionMatricula){

        solicitudAmpliacionMatricula.setAlumno(alumno);
        alumno.getSolicitudAmpliacionMatricula().add(solicitudAmpliacionMatricula);
        ampliacionMatriculaServicio.save(solicitudAmpliacionMatricula);
        Long id = solicitudAmpliacionMatricula.getAsignatura().getId();
        return "redirect:/alum/curso/ampliacion/estado/"+id;
    }

    @GetMapping("/curso/ampliacion/estado/{id}")
    public String ampliacionEstado(@PathVariable("id") Long id, @AuthenticationPrincipal Alumno alumno, Model model){
        Asignatura asignatura = asignaturaServicio.findById(id).get();
        model.addAttribute("alumno", alumno);
        model.addAttribute("curso", alumno.getCurso());
        model.addAttribute("asignatura", asignatura);
        model.addAttribute("ampliacion", asignaturaServicio.buscarAmpliacionMatricula(asignatura,alumno));
        return "/alumno/ampliacion_estado";
    }
}
