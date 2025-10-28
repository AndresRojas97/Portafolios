package com.andres.portafolio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 1. Le dice a Spring que esta es una clase de configuración
public class SecurityConfig {

    // --- PARTE A: EL CODIFICADOR DE CONTRASEÑAS ---

    @Bean // 2. Le dice a Spring que este método crea un "Bean" (componente) gestionado
    public PasswordEncoder passwordEncoder() {
        // Usamos BCrypt, el estándar de la industria para hashear contraseñas
        return new BCryptPasswordEncoder();
    }


    // --- PARTE B: LAS REGLAS DE SEGURIDAD (EL FILTRO) ---

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        // 3. Añadimos "/registro" a la lista pública
                        .requestMatchers("/", "/estilos.css", "/registro").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // La URL que activa el logout
                        .logoutSuccessUrl("/login") // A dónde redirigir después de un logout exitoso
                        .permitAll() // Permite a todos acceder a la URL de logout
                );

        return http.build();
    }
}
