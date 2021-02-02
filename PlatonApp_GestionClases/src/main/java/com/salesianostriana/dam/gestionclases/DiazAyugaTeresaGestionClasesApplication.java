package com.salesianostriana.dam.gestionclases;

import com.salesianostriana.dam.gestionclases.email.EmailService;
import com.salesianostriana.dam.gestionclases.model.*;
import com.salesianostriana.dam.gestionclases.service.*;
import com.salesianostriana.dam.gestionclases.upload.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class DiazAyugaTeresaGestionClasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiazAyugaTeresaGestionClasesApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UsuarioServicio usuarioServicio, BCryptPasswordEncoder passwordEncoder,
								  TituloServicio tituloServicio, CursoServicio cursoServicio,
								  AsignaturaServicio asignaturaServicio, HorarioServicio horarioServicio,
								  EmailService emailService, StorageService storageService,
								  EscuelaServicio escuelaServicio, SituacionExcepcionalServicio excepcionalServicio,
								  SolicitudAmpliacionMatriculaServicio ampliacionServicio) {
		return args -> {

			//storageService.deleteAll();
			storageService.init();


			//escuelaServicio.cargarEstructuraEscuela("escuela");
			escuelaServicio.cargarEstructuraEscuela2("dam");
			escuelaServicio.cargarAlumnos("alumnos_dam");
			//escuelaServicio.cargarHorarios("horarios");

			for(Usuario usuario: usuarioServicio.findAll()){
				usuario.setCodigoInvitacion("11111");
				usuarioServicio.edit(usuario);
			}



			Profesor jefeE = new Profesor();
			jefeE.setAdmin(true);
			jefeE.setNombre("Ángel");
			jefeE.setApellidos("Naranjo");
			jefeE.setEmail("angel@email.com");
			jefeE.setPassword(passwordEncoder.encode("1234"));
			usuarioServicio.save(jefeE);

			Profesor u = new Profesor();
			u.setAdmin(false);
			u.setNombre("Luismi");
			u.setApellidos("López");
			u.setEmail("luismi@email.com");
			u.setPassword(passwordEncoder.encode("1234"));
			usuarioServicio.save(u);

			Alumno alumno = new Alumno();
			alumno.setNombre("Teresa");
			alumno.setApellidos("Díaz");
			alumno.setEmail("teresa@email.com");
			alumno.setPassword(passwordEncoder.encode("1234"));
			//alumno.setCodigoInvitacion("11111");
			alumno.setFechaNacimiento(LocalDate.of(1991,12,17));
			alumno.setCurso(cursoServicio.buscarPorNombre("1º D.A.M."));

			usuarioServicio.save(alumno);



			//System.out.println(horarioServicio.findById(19L).get().getDia());
//			System.out.println("Antes de situacion exepcional");
//
//
//			for (Asignatura a: usuarioServicio.asignaturasMatriculadas(alumno)){
//				System.out.println(a);
//			}
//
//			SituacionExcepcional s = new SituacionExcepcional(TipoSituacionExpecional.CONVALIDACION,
//					alumno, asignaturaServicio.buscarPorNombre("Programación", "1º D.A.M."));
//			s.setEstadoSolicitud(EstadoSolicitud.CONFIRMADA);
//			excepcionalServicio.save(s);
//
//			SolicitudAmpliacionMatricula ampliacion = new SolicitudAmpliacionMatricula(alumno,
//					asignaturaServicio.buscarPorNombre("Formación y Orientación Personal", "2º D.A.M."));
//			alumno.getSolicitudAmpliacionMatricula().add(ampliacion);
//			ampliacion.setEstadoSolicitud(EstadoSolicitud.CONFIRMADA);
//
//			ampliacionServicio.save(ampliacion);
//			usuarioServicio.edit(alumno);

//			System.out.println("--------------------Horas libres-----------------------------");
//			System.out.println("--------------------Horas libres-----------------------------");
//			System.out.println("--------------------Horas libres-----------------------------");
//
//			for(Horario h: usuarioServicio.horarioLibreSegundoCursoDisponibles(alumno)){
//				System.out.println(h);
//			}
//
//			System.out.println("Asignaturas de segundo disponibles");
//			for (Asignatura a: usuarioServicio.asignaturasSegundoCursoDisponibles(alumno)){
//				System.out.println(a);
//			}
//
//			for (Horario horarios: usuarioServicio.obtenerHorarioAlumno(alumno)){
//				System.out.println(horarios);
//			}
//
//			Asignatura asignatura = asignaturaServicio.buscarPorNombre("Inglés", "2º D.A.M.");
//			ExcepcionPK pk = new ExcepcionPK(alumno.getId(), asignatura.getId());
//
//			Asignatura asignatura2 = asignaturaServicio.buscarPorNombre("Acceso a Datos", "2º D.A.M.");
//
//			//asignatura2.setActivo(false);
//			for (Horario h: asignatura.getHorarios()){
//				h.setActivo(false);
//				horarioServicio.edit(h);
//			}
//			asignaturaServicio.edit(asignatura);

//			for (Asignatura a: usuarioServicio.asignaturasMatriculadas(alumno)){
//				alumno.getAsignaturas().add(a);
//			}
//			usuarioServicio.edit(alumno);

//			for(Usuario a : usuarioServicio.findAllAlumnos()){
//				Alumno alum= (Alumno)a;
//				for(Asignatura asig: alum.getCurso().getAsignaturas()){
//					alum.getAsignaturas().add(asig);
//				}
//				usuarioServicio.edit(alum);
//			}



			//asignaturaServicio.edit(asignatura2);

			//System.out.println(excepcionalServicio.buscarPorAlumnoAsignatura(alumno,asignatura));
//
//			System.out.println("\n\nDespués de situacion exepcional");
//
//			for (Asignatura a: usuarioServicio.asignaturasMatriculadas(alumno)){
//				System.out.println(a);
//			}


//
//
//
//			System.out.println(asignaturaServicio.buscarSituacionExcepcional(asignaturaServicio.findById(5L).get(),
//			alumno));
//			Titulo titulo = Titulo.builder().nombre("Tecnico Superior Desarrollo Aplicaciones Multiplataforma")
//					.activo(true).build();
//			tituloServicio.save(titulo);
//			Curso curso = Curso.builder().nombre("2º DAM").activo(true).titulo(titulo).build();
//			cursoServicio.save(curso);
//			//titulo.addCurso(curso);
//			//cursoServicio.edit(curso);
//
//			Titulo titulo2 = Titulo.builder().nombre("Tecnico Superior Administración y Finanzas").activo(false).build();
//			tituloServicio.save(titulo2);

//			Asignatura asignatura = Asignatura.builder().nombre("Acceso a Datos").activo(true).curso(curso).build();
//
//			asignaturaServicio.save(asignatura);
//
//			Horario h1 = Horario.builder().dia("L").tramoHorario(1).asignatura(asignatura).build();
//			Horario h2 = Horario.builder().dia("M").tramoHorario(4).asignatura(asignatura).build();
//			Horario h3 = Horario.builder().dia("X").tramoHorario(3).asignatura(asignatura).build();
//
////			asignatura.addHorario(h1);
////			asignatura.addHorario(h2);
////			asignatura.addHorario(h3);
//
//			horarioServicio.save(h1);
//			horarioServicio.save(h2);
//			horarioServicio.save(h3);

			//emailService.sendEmail("tdayuga10@gmail.com", "Prueba de correo 2",
			//		"Esto es un email enviado desde la aplicación");

			/*String codigo = usuarioServicio.codigoInvitacion();
			System.out.println("Código de invitación: " + codigo);

			Usuario teresa = usuarioServicio.findAllAlumnos().get(0);
			teresa.setCodigoInvitacion(codigo);
			usuarioServicio.edit(teresa);*/

//			Curso c = cursoServicio.findAll().get(0);
//
//			List<Horario> horario = horarioServicio.obtenerHorario(c);
//			for(Horario h : horario){
//				if(h.getTramoHorario().equalsIgnoreCase("1")){
//					horario.add(h);
//				}
//			}
//
			//System.out.println(horarioServicio.obtenerHorario(cursoServicio.buscarPorNombre("2º de D.A.M.")));
//			for(Horario h: horarioServicio.obtenerHorario(cursoServicio.buscarPorNombre("2º D.A.M."))){
//				System.out.println(h);
//			}

//			for (List<Horario> horarios: horarioServicio.horarioPorTramos(cursoServicio.buscarPorNombre("2º D.A.M."))){
//				System.out.println(horarios);
//			}

//			Asignatura a = asignaturaServicio.buscarPorNombre("Inglés", "2º D.A.M.");
//			a.setActivo(false);
//			asignaturaServicio.edit(a);
//
//			for (List<Horario> horarios :horarioServicio.horarioPorTramos(cursoServicio.buscarPorNombre("2º D.A.M."))){
//				System.out.println(horarios);
//			}
















		};
	}

}
