package com.dearlavion.notification.config;

import com.dearlavion.notification.security.TokenVerificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenVerificationFilter tokenVerificationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // ✅ REQUIRED FOR CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // SPRING
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // PUBLIC
                        .requestMatchers("/auth/**").permitAll()
                        // AUTHENTICATED
                        .requestMatchers("/notification/subscription/**").authenticated()
                        // EVERYTHING ELSE
                        .anyRequest().permitAll()
                )
                .addFilterBefore(tokenVerificationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ Define your CORS rules here
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                // 🔐 Dev
                "http://dearlavion.site",
                "https://dearlavion.site",
                "https://www.dearlavion.site",
                "http://localhost",
                "http://127.0.0.1",
                "http://157.173.97.37",
                "http://localhost:4200",
                "https://*.ngrok.pizza"
        )); // your Angular app
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH"
        ));
        configuration.setAllowedHeaders(List.of(
                "Content-Type",
                "Authorization",
                "Accept",
                "Origin"
        ));
        configuration.setAllowCredentials(true); // needed if you send cookies or Authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
