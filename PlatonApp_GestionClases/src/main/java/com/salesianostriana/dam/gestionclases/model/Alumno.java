package com.salesianostriana.dam.gestionclases.model;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Alumno extends Usuario{

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "alumno", fetch = FetchType.EAGER)
    private List<SituacionExcepcional> situacionExcepcional = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "alumno")
    private List<SolicitudAmpliacionMatricula> solicitudAmpliacionMatricula = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Curso curso;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            joinColumns = @JoinColumn(name="alumno_id"),
            inverseJoinColumns = @JoinColumn(name="asignatura_id")
    )
    private Set<Asignatura> aprobadas = new HashSet<>();





    public Alumno(String nombre, String apellidos, String email, LocalDate fechaNacimiento, Curso curso) {
        super(nombre, apellidos, email);
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
    }

    public Alumno(String nombre, String apellidos, String email, LocalDate fechaNacimiento, Curso curso, Set<Asignatura> aprobadas) {
        super(nombre, apellidos, email);
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
        this.aprobadas = aprobadas;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ALUM"));
    }

    //Métodos helpers Asignatura
    public void addAsignatura(Asignatura a) {
        aprobadas.add(a);
        a.getAlumnos().add(this);
    }

    public void removeAsignatura(Asignatura a) {
        aprobadas.remove(a);
        a.getAlumnos().remove(this);
    }
}
