package com.andres.portafolio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class PortafolioController {

    @GetMapping("/")
    public String inicio(Model model){
        model.addAttribute("titulo", "Mi portafolio Dinámico (desde Java)");


        List<Proyecto> proyectos = new ArrayList<>();
        proyectos.add(new Proyecto("Proyecto App de Clima", "Una app que consume una API de clima."));
        proyectos.add(new Proyecto("E-commerce basico", "Tienda en linea creada con Spring Boot y JPA."));
        proyectos.add(new Proyecto("Clon de google", "Un proyecto de HTML y CSS replicando al buscador."));

        model.addAttribute("proyectos", proyectos);

        return "index";
    }
}
