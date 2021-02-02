package com.salesianostriana.dam.gestionclases.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ExcepcionPK implements Serializable {

    private static final long serialVersionUID = 8682909319466153524L;

    private long alumno_id;
    private long asignatura_id;


}
