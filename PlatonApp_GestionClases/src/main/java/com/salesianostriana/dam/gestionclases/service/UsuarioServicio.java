package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.email.EmailService;
import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.repository.UsuarioRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServicio extends ServicioBaseImpl<Usuario, Long, UsuarioRepository> {

    private final EmailService emailService;
    private final AsignaturaServicio asignaturaServicio;
    private final CursoServicio cursoServicio;

    public Optional<Usuario> buscarPorEmail(String email) {

        return repositorio.findFirstByEmail(email);
    }

    public List<Usuario> findAllAlumnos() {
        return this.findAll().stream().filter(u -> u instanceof Alumno).collect(Collectors.toUnmodifiableList());
    }

    public List<Usuario> findAlumnosByCurso(Curso curso){

        return curso.getAlumnos().stream().sorted(Comparator.comparing(Usuario::getApellidos))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Usuario> findAllProfesores() {
        return this.findAll().stream().filter(u -> u instanceof Profesor).collect(Collectors.toUnmodifiableList());

    }


    public void enviarCodigoUsuario(Usuario usuario) {
        String codigo = codigoInvitacion();
        usuario.setCodigoInvitacion(codigo);

        emailService.sendEmail(usuario.getEmail(), "Código de invitación",
                "Su código de invitación es: " + codigo);
    }

    public String codigoInvitacion() {
        String codigo = "";
        Random r = new Random(System.nanoTime());
        for (int i = 0; i < 5; i++) {

            codigo += r.nextInt(10);
        }


        return codigo;


    }

    public boolean codigoCorrecto(String email, String codigo){
        boolean correcto = false;
        Usuario usuario = this.buscarPorEmail(email).get();
        if(usuario != null && usuario.getCodigoInvitacion() != null){
            if(usuario.getCodigoInvitacion().equals(codigo)){
                correcto = true;
            }

        }
        return correcto;
    }

    public boolean cambioPassword(CambioPassword cambioPassword, BCryptPasswordEncoder passwordEncoder){

        Usuario usuario;
        boolean correcto = false;
        if (cambioPassword.getPass1().equals(cambioPassword.getPass2())){
            usuario = this.buscarPorEmail(cambioPassword.getEmail()).get();
            usuario.setPassword(passwordEncoder.encode(cambioPassword.getPass1()));
            usuario.setCodigoInvitacion(null);
            this.edit(usuario);
            correcto = true;
        }

        return correcto;

    }



    public EstadoMatricula estadoMatricula(Alumno alumno, Asignatura asignatura){

        List<Asignatura> asigCurso = alumno.getCurso().getAsignaturas().stream().collect(Collectors.toList());
        List<Asignatura> asignAlumno = alumno.getAprobadas().stream().collect(Collectors.toList());
        SituacionExcepcional situacionExcepcional = asignaturaServicio.buscarSituacionExcepcional(asignatura, alumno);

        EstadoMatricula estado = null;
        if(situacionExcepcional == null){

            if(alumno.getAprobadas().isEmpty()){
                estado = EstadoMatricula.MATRICULADO;


            }

            else{

                if (alumno.getAprobadas().contains(asignatura)) {
                    estado = EstadoMatricula.APRO_CURSO_ANTERIOR;
                }
                else{
                    estado = EstadoMatricula.MATRICULADO;
                }
            }


        }
        else{
            if(situacionExcepcional.getEstadoSolicitud() == EstadoSolicitud.CONFIRMADA){
                if(situacionExcepcional.getTipo() == TipoSituacionExpecional.CONVALIDACION){
                    estado =  EstadoMatricula.CONVALIDADO;
                }
                else if(situacionExcepcional.getTipo() == TipoSituacionExpecional.EXENCION){
                    estado =  EstadoMatricula.EXENTO;
                }
            }
            else{
                estado = EstadoMatricula.MATRICULADO;
            }


        }

        return estado;

    }

    public boolean matriculado(Asignatura asignatura, Alumno alumno){
        boolean matriculado = true;
        if(asignatura.getSituacionExcepcional()!=null){
            for(SituacionExcepcional s: asignatura.getSituacionExcepcional()){
                if(s.getAlumno() == alumno){
                    matriculado = false;
                }
            }
        }

        return matriculado;

    }

    public List<Asignatura> asignaturasMatriculadas(Alumno alumno){
        List<Asignatura> matriculadas = new ArrayList<>();
        SituacionExcepcional situacionExcepcional;

        Set<Asignatura> asignaturas = alumno.getCurso().getAsignaturas().stream()
                .filter(Asignatura::isActivo).collect(Collectors.toSet());
        for (Asignatura a: asignaturas){
            situacionExcepcional = asignaturaServicio.buscarSituacionExcepcional(a, alumno);
            if(situacionExcepcional == null ||
                    situacionExcepcional.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE ||
                    situacionExcepcional.getEstadoSolicitud() == EstadoSolicitud.DENEGADA){
                matriculadas.add(a);

            }
        }
        if(!alumno.getAprobadas().isEmpty()){
            for(Asignatura a: alumno.getAprobadas()){
                matriculadas.remove(a);
            }
        }
        if(!alumno.getSolicitudAmpliacionMatricula().isEmpty()){
            for(SolicitudAmpliacionMatricula ampliacion: alumno.getSolicitudAmpliacionMatricula()){
                matriculadas.add(ampliacion.getAsignatura());
            }
        }



        return matriculadas;
    }

    public List<Horario> obtenerHorarioAlumno(Alumno alumno){
        List<Horario> horario = new ArrayList<>();
        SituacionExcepcional situacionExcepcional;
        List<Asignatura> asignaturas = alumno.getCurso().getAsignaturas().stream().collect(Collectors.toList());
        List<Asignatura> asignaturasAlumno = alumno.getAprobadas().stream().collect(Collectors.toList());
        List<Horario> libres = new ArrayList<>();
        for(Asignatura a : asignaturas){
            situacionExcepcional = asignaturaServicio.buscarSituacionExcepcional(a, alumno);
            for (Horario h : a.getHorarios()){

                if(situacionExcepcional == null ||
                        situacionExcepcional.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE ||
                        situacionExcepcional.getEstadoSolicitud() == EstadoSolicitud.DENEGADA){

                        if(asignaturasAlumno.contains(a)){
                            h.getAsignatura().setActivo(false);
                            libres.add(h);
                        }

                }
                else{
                    h.getAsignatura().setActivo(false);
                    libres.add(h);

                }
                horario.add(h);

            }
        }

        if(!alumno.getSolicitudAmpliacionMatricula().isEmpty()){
            for(SolicitudAmpliacionMatricula ampliacion: alumno.getSolicitudAmpliacionMatricula()){
                for(Horario h: ampliacion.getAsignatura().getHorarios()){
                    horario.add(h);
                    for (Horario libre: libres){
                        if(libre.getDia() == h.getDia() && libre.getTramoHorario() == h.getTramoHorario()){

                            horario.remove(libre);

                        }
                    }
                }
            }
        }




        return horario;
    }

    public List<List<Horario>> horarioPorTramosAlumno(Alumno alumno){
        int tramo = 1;

        List<List<Horario>> listaHorarios = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            int finalTramo = tramo;

            listaHorarios.add(obtenerHorarioAlumno(alumno).stream()
                    .filter(h -> h.getTramoHorario()== finalTramo).sorted(Comparator.comparing(Horario::getDia))
                    .collect(Collectors.toList()));

            tramo++;
        }

        for (int i = 0; i < listaHorarios.size(); i++) {

            if (listaHorarios.get(i).size() < 5){
                while(listaHorarios.get(i).size()!=5){
                    listaHorarios.get(i).add(new Horario((i+1),null));
                }

            }

        }


        return listaHorarios;
    }

    public List<Asignatura> asignaturasSegundoCurso(Alumno alumno){
        List<Asignatura> asignaturasSegundo = new ArrayList<>();
        Titulo titulo = alumno.getCurso().getTitulo();
        Curso curso = cursoServicio.buscarCursoSegundo(titulo);

        asignaturasSegundo = curso.getAsignaturas().stream().filter(a->a.isActivo()).collect(Collectors.toList());

        return asignaturasSegundo;
    }

    public List<Asignatura> asignaturasSegundoCursoDisponibles(Alumno alumno){
        List<Asignatura> asignaturasDisponibles = new ArrayList<>();
        List<Asignatura> asignaturasSegundo = asignaturasSegundoCurso(alumno);
        List<Asignatura> asignaturasMatriculadas = this.asignaturasMatriculadas(alumno);
        List<Horario> horasLibres = new ArrayList<>();
        boolean encontrado = false;

        for(Horario h: this.obtenerHorarioAlumno(alumno)){
            if(!h.getAsignatura().isActivo()){
                horasLibres.add(h);
            }
        }

        for(Asignatura a: asignaturasSegundo){

            for (int i = 0; i < a.getHorarios().size() && encontrado; i++) {
                for(Horario libre: horasLibres){

                }

                if(horasLibres.contains(a.getHorarios().get(i))){
                    encontrado = true;
                }else {
                    encontrado = false;
                }
            }


            if(encontrado){
                asignaturasDisponibles.add(a);
            }
            encontrado = true;
        }




        return asignaturasDisponibles;
    }

    public List<Horario> horarioLibreSegundoCursoDisponibles(Alumno alumno){
        List<Asignatura> asignaturasDisponibles = new ArrayList<>();
        List<Asignatura> asignaturasSegundo = asignaturasSegundoCurso(alumno);
        List<Asignatura> asignaturasMatriculadas = this.asignaturasMatriculadas(alumno);
        List<Horario> horasLibres = new ArrayList<>();

        for(Horario h: this.obtenerHorarioAlumno(alumno)){
            if(!h.getAsignatura().isActivo()){
                horasLibres.add(h);
            }
        }

        return horasLibres;
    }



}
