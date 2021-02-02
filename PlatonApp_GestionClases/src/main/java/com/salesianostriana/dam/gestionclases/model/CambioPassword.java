package com.salesianostriana.dam.gestionclases.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CambioPassword {

    private String email;
    private String pass1;
    private String pass2;

    public CambioPassword(String email) {
        this.email = email;
    }
}
