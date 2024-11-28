package com.prod.ib3.configuration;

import com.prod.ib3.services.AllService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    AllService allService;

    /*@Autowired
    @Lazy
    com.prod.ib3.security.jwt.JwtRequestFilter jwtRequestFilter;*/

    /*@Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("X-XSRF-TOKEN");
        return repository;
    }*/

    /*@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(allService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }*/

    /*@Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .authenticationProvider(authenticationProvider());

        return authenticationManagerBuilder.build();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public com.prod.ib3.security.jwt.JwtRequestFilter jwtRequestFilter(AuthenticationManager authManager) {
        return new com.prod.ib3.security.jwt.JwtRequestFilter(authManager, "${jwt.secret}");
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
                //.apply(rememberMeConfigurer())
                //.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/product")
                .authenticated()
                .requestMatchers(HttpMethod.POST, "/search")
                .authenticated()
                .requestMatchers(HttpMethod.POST, "/order")
                .authenticated()
                .requestMatchers(HttpMethod.DELETE, "/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .logout().logoutUrl("/auth/logout");

        //http.authenticationProvider(authenticationProvider());

        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*@Bean
    public RememberMeConfigurer<HttpSecurity> rememberMeConfigurer() {
        return new RememberMeConfigurer<HttpSecurity>()
                .userDetailsService(allService)
                .key("${jwt.secret}")
                .rememberMeCookieName("${jwt.cookie}")
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(86400);
    }*/

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/
}
