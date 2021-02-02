package com.salesianostriana.dam.gestionclases.service;

import com.salesianostriana.dam.gestionclases.model.Asignatura;
import com.salesianostriana.dam.gestionclases.model.Curso;
import com.salesianostriana.dam.gestionclases.model.Titulo;
import com.salesianostriana.dam.gestionclases.repository.TituloRepository;
import com.salesianostriana.dam.gestionclases.service.base.ServicioBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TituloServicio extends ServicioBaseImpl<Titulo, Long, TituloRepository> {

    @Autowired
    private CursoServicio cursoServicio;


    public List<Titulo> titulosActivos(){
        return this.findAll().stream().filter(t -> t.isActivo()).collect(Collectors.toUnmodifiableList());
    }

    public Titulo buscarPorNombre(String nombre){
        return repositorio.findByName(nombre);
    }

    public void cargarTitulos(String nombre){
        List<Titulo> titulos = new ArrayList<>();
        String path = "./upload-dir/" + nombre + ".csv";

        try{
            titulos = Files.lines(Paths.get(ResourceUtils.getFile(path).toURI())).skip(1).map(line -> {
                String value = line;
                return new Titulo(value);
            }).collect(Collectors.toUnmodifiableList());
            for (Titulo p : titulos){
                this.save(p);
            }
        } catch (Exception e){
            System.err.println("Error de lectura");
            System.exit(-1);
        }
    }






}
