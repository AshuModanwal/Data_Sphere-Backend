package com.datasphere.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // ← pull in the values you set in application.properties
    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String rawPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create an in-memory user with the username/password from props,
     * BCrypt-encoded on startup.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username(username)
                .password(encoder.encode(rawPassword))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) disable CSRF for a stateless REST API
                .csrf(AbstractHttpConfigurer::disable)

                // 2) public endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/signup",
//                                "/auth/login",
                                "/auth/profileData"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 3) require HTTP Basic for everything else
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
