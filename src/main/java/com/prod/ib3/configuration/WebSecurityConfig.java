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
            .password(passwordEncoder().encode("teste123"))
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
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/admin-newsletter").hasRole("ADMIN")
                .requestMatchers("/items").hasRole("ADMIN")
                .requestMatchers("/items/add").hasRole("ADMIN")
                .requestMatchers("/remove/{id}").hasRole("ADMIN")
                .requestMatchers("/dashboard").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/admin-senha")  // Página de login customizada
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin", true) // Redireciona para /admin após login
                .failureUrl("/admin-senha?error=true") // Redireciona para /admin-senha com erro se falhar
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // Redireciona para / após logout
                .permitAll()
            );
        return http.build();
    }
}