package com.andres.portafolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 1. Importa el Encoder
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // 2. Importa PostMapping
import org.springframework.web.bind.annotation.RequestParam; // 3. Importa RequestParam

import java.util.ArrayList;
import java.util.List;

@Controller
public class PortafolioController {

    // --- 4. INYECTAMOS LAS HERRAMIENTAS ---
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired // Le pide a Spring que nos pase las herramientas
    public PortafolioController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Necesitamos esto para ENCRIPTAR
    }

    // --- El método que ya teníamos ---
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("titulo", "Mi portafolio Dinámico (desde Java)");
        // (Tu lógica de la lista de proyectos sigue aquí...)
        List<Proyecto> proyectos = new ArrayList<>();
        proyectos.add(new Proyecto("Proyecto App de Clima", "Una app que consume una API de clima."));
        proyectos.add(new Proyecto("E-commerce basico", "Tienda en linea creada con Spring Boot y JPA."));
        proyectos.add(new Proyecto("Clon de google", "Un proyecto de HTML y CSS replicando al buscador."));
        model.addAttribute("proyectos", proyectos);
        return "index";
    }

    // --- 5. MÉTODOS NUEVOS PARA EL REGISTRO ---

    /**
     * Muestra la página de registro.
     * Se activa cuando alguien visita GET /registro
     */
    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro() {
        return "registro"; // Muestra el archivo registro.html
    }

    /**
     * Procesa los datos del formulario de registro.
     * Se activa cuando el formulario envía datos a POST /registro
     */
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String username, @RequestParam String password) {

        // 1. Encriptamos la contraseña ANTES de guardarla
        String passwordEncriptada = passwordEncoder.encode(password);

        // 2. Creamos el nuevo objeto User
        User nuevoUsuario = new User(username, passwordEncriptada);

        // 3. Guardamos el usuario en la base de datos (PostgreSQL)
        userRepository.save(nuevoUsuario);

        // 4. Redirigimos al usuario a la página de login
        return "redirect:/login";
    }

    /**
     * Muestra la página de login personalizada.
     * Se activa cuando alguien visita GET /login
     */
    @GetMapping("/login")
    public String mostrarFormularioDeLogin() {
        return "login"; // Muestra el archivo login.html
    }
}
