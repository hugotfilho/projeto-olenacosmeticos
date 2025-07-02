package com.prod.ib3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("olenacosmeticos2025*"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/admin",
                                "/admin-newsletter",
                                "/items",
                                "/items/add",
                                "/remove/**",
                                "/dashboard",
                                "/view-newsletters",
                                "/messages"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/home", "/sobre", "/contato", "/catalogo",
                                "/newsletter/subscribe", "/newsletter/send",
                                "/messages/add", "/all", "/{id}"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/admin-senha") // Página personalizada de login
                        .loginProcessingUrl("/login") // URL que processa o POST do login
                        .defaultSuccessUrl("/admin", true) // Redirecionamento após login
                        .failureUrl("/admin-senha?error=true")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Temporariamente desativado para evitar 403 nos POST públicos

        return http.build();
    }
}
