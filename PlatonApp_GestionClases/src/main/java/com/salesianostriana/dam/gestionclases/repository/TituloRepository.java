package com.salesianostriana.dam.gestionclases.repository;

import com.salesianostriana.dam.gestionclases.model.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

    @Query("select distinct t from Titulo t join fetch t.cursos")
    List<Titulo> findAllJoin();

    @Query("select t from Titulo t where t.nombre = :nombre")
    Titulo findByName(@Param("nombre") String nombre);
}
