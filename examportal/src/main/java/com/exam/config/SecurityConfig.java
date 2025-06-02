package com.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.exam.security.jwt.JwtAuthEntryPoint;
import com.exam.security.jwt.JwtFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtAuthEntryPoint authEntryPoint, JwtFilter jwtFilter) {
        this.authEntryPoint = authEntryPoint;
        this.jwtFilter = jwtFilter;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(AbstractHttpConfigurer::disable)
//            .exceptionHandling(exception -> exception
//                .authenticationEntryPoint(authEntryPoint)
//            )
//            .sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .authorizeHttpRequests(auth -> auth
//                // Public endpoints
//                .requestMatchers("/api/auth/signin", "/api/auth/signup", "/api/auth/text").permitAll()
//
//                // Role-based access for specific auth paths
//                .requestMatchers("/api/auth/admin-only").hasRole("ADMIN")
//
//
//
//                // All other requests require authentication
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - permit all without authentication
            		
            			    .requestMatchers(
            			        "/user/signin",
            			        "/user/signup",
            			        "/user/text",
            			        "/user/"  // public endpoints
            			    ).permitAll()
            			    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow preflight requests
            			 
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

////////



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}