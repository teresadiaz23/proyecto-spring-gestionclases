package com.salesianostriana.dam.gestionclases.repository;

import com.salesianostriana.dam.gestionclases.model.Curso;
import com.salesianostriana.dam.gestionclases.model.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query("select c from Curso c where c.nombre = :nombre")
    Curso findByName(@Param("nombre") String nombre);

    @Query("select c from Curso c where lower(c.nombre) like '%2º%' and c.titulo = :titulo")
    Curso cursoSegundo(@Param("titulo") Titulo titulo);


}
