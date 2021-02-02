package com.salesianostriana.dam.gestionclases.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profesor extends Usuario{

    private boolean admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_";
        if (admin) {
            role += "ADMIN";
        } else {
            role += "TEACHER";
        }
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }


}
