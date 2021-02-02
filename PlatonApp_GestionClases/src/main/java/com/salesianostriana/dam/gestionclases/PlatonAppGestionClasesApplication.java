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

@SpringBootApplication
public class PlatonAppGestionClasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatonAppGestionClasesApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UsuarioServicio usuarioServicio, BCryptPasswordEncoder passwordEncoder,
								  CursoServicio cursoServicio, StorageService storageService,
								  EscuelaServicio escuelaServicio) {
		return args -> {

			//storageService.deleteAll();
			storageService.init();



			escuelaServicio.cargarEstructuraEscuela2("dam");
			escuelaServicio.cargarAlumnos("alumnos_dam");


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
			alumno.setFechaNacimiento(LocalDate.of(1991,12,17));
			alumno.setCurso(cursoServicio.buscarPorNombre("1º D.A.M."));

			usuarioServicio.save(alumno);


		};
	}

}
