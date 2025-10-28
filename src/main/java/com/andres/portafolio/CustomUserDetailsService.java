package com.andres.portafolio;

// Importa todas las clases necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service // 1. Le dice a Spring que esta clase es un "Servicio" (un componente)
public class CustomUserDetailsService implements UserDetailsService { // 2. Implementa la interfaz

    // 3. Inyecta nuestro repositorio de usuarios
    private final UserRepository userRepository;

    @Autowired // (Opcional en versiones modernas, pero bueno para claridad)
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override // 4. Este es el único método que Spring Security nos pide implementar
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 5. Busca al usuario en nuestra base de datos (PostgreSQL)
        //    Usamos el método mágico que creamos en el UserRepository
        User usuarioDeNuestraBD = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 6. Convierte nuestro User (de JPA) en un User (de Spring Security)
        //    Spring necesita un objeto "UserDetails". Usamos la implementación que ya trae:
        //    Recibe: username, password, y una lista de "roles" (autoridades).
        //    Por ahora, le pasamos una lista de roles vacía (new ArrayList<>()).
        return new org.springframework.security.core.userdetails.User(
                usuarioDeNuestraBD.getUsername(),
                usuarioDeNuestraBD.getPassword(),
                new ArrayList<>()
        );
    }
}