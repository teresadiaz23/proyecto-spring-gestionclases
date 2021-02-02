package com.salesianostriana.dam.gestionclases.repository;

import com.salesianostriana.dam.gestionclases.model.Curso;
import com.salesianostriana.dam.gestionclases.model.SituacionExcepcional;
import com.salesianostriana.dam.gestionclases.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findFirstByEmail(String email);



}
