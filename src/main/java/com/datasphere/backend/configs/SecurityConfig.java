package com.datasphere.backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) Disable CSRF for a pure REST API
                .csrf(AbstractHttpConfigurer::disable)

                // 2) Which URLs to allow without login
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signup").permitAll()   // public
                        .anyRequest().authenticated()                  // everything else needs auth
                )

                // 3) Use HTTP Basic, passing your spring.security.user.name/pass
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
