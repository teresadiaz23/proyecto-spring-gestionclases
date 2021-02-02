package com.salesianostriana.dam.gestionclases.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class ServicioBaseImpl <T, ID, R extends JpaRepository<T, ID>> implements ServicioBase<T, ID, R>{

//    protected R repositorio;
//
//    public ServicioBaseImpl(R repo) {
//        this.repositorio = repo;
//    }

    @Autowired
    protected R repositorio;


    @Override
    public T save(T t) {
        return repositorio.save(t);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repositorio.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repositorio.findAll();
    }

    @Override
    public T edit(T t) {
        return repositorio.save(t);
    }

    @Override
    public void delete(T t) {
        repositorio.delete(t);
    }

    @Override
    public void deleteById(ID id) {
        repositorio.deleteById(id);
    }
}
