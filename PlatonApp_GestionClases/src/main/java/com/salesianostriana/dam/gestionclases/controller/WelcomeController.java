package com.salesianostriana.dam.gestionclases.controller;

import com.salesianostriana.dam.gestionclases.model.CambioPassword;
import com.salesianostriana.dam.gestionclases.model.Usuario;
import com.salesianostriana.dam.gestionclases.service.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WelcomeController {

    private final UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(){
        return "redirect:/login";
    }

    @GetMapping("/codigo")
    public String codigoInvitacion(@RequestParam(name = "codigo", required = false) String codigo,
                                   @RequestParam(name = "email", required = false) String email, Model model){
        model.addAttribute("codigo", codigo);
        model.addAttribute("email", email);
        return "login_codigo";
    }

    @PostMapping("/codigo/correcto")
    public String codigoCorrecto(@ModelAttribute("codigo") String codigo, @ModelAttribute("email") String email,
                                 Model model){

        model.addAttribute("cambio", new CambioPassword(email));

        if(usuarioServicio.codigoCorrecto(email, codigo)){

            return "cambiar_password";

        }else{
            return "redirect:/codigo";

        }

    }

    @PostMapping("/password/correcta")
    public String passwordCambiada(@ModelAttribute("cambio") CambioPassword cambioPassword,
                                   BCryptPasswordEncoder passwordEncoder){

        if(usuarioServicio.cambioPassword(cambioPassword, passwordEncoder)){
            return "redirect:/";
        }
        else{
            return "redirect:/codigo/correcto";
        }

    }



}
