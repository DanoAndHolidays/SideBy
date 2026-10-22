package com.sideby.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.http.HttpStatus;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
        // Spring Security 7 使用带 UserDetailsService 参数的构造方法。
        DaoAuthenticationProvider provider =
            new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository =
            CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setHeaderName("X-CSRF-TOKEN");
        repository.setCookieName("XSRF-TOKEN");
        return repository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        CsrfTokenRepository csrfTokenRepository) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/csrf",
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/system/status",
                    "/actuator/health"
                )
                .permitAll()
                .anyRequest()
                .authenticated())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(
                        "{\"code\":\"OK\",\"message\":\"success\",\"data\":null}");
                }))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authenticationException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(
                        "{\"code\":\"UNAUTHENTICATED\",\"message\":\"Authentication is required\",\"data\":null}");
                }));

        return http.build();
    }
}
