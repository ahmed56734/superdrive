package com.ahmeds.superdrive.security;

import com.ahmeds.superdrive.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> {
                    form.loginPage("/login").permitAll();
                    form.defaultSuccessUrl("/home", true);
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/signup", "/login", "/css/**", "/js/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint((request, response, authException) -> {
                        // Avoid redirect loop for the /login endpoint
                        if (request.getRequestURI().equals("/login")) {
                            response.sendRedirect("/login");
                            return;
                        }

                        // Redirect to /login with original query parameters
                        String queryString = request.getQueryString();
                        String targetUrl = "/login";
                        if (queryString != null) {
                            targetUrl += "?" + queryString;
                        }
                        response.sendRedirect(targetUrl);
                    });
                })
                .authenticationProvider(authenticationService)
                .build();
    }
}
