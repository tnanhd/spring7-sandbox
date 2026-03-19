package com.example.bootformlogin.infra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] AUTH_WHITELIST = {
    "/",
    "/index.html",
    "/static/**",
    "/assets/**",
    "/*.json",
    "/*.css",
    "/*.js",
    "/*.png",
    "/*.jpg",
    "/*.ico"
  };

  @Bean
  public SecurityFilterChain oneSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(Customizer.withDefaults())
        .csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
        .formLogin(
            form ->
                // Default login url is /login
                form.permitAll()
                    .successHandler(
                        (_, response, _) -> {
                          response.setStatus(HttpServletResponse.SC_OK);
                          response.getWriter().write("Login Successful");
                        })
                    .failureHandler(
                        (_, response, exception) -> {
                          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                          response.getWriter().write("Login Failed: " + exception.getMessage());
                        }))
        .logout(
            logout ->
                // Default logout url is /logout
                logout.logoutSuccessHandler(
                    (_, response, _) -> {
                      response.setStatus(HttpServletResponse.SC_OK);
                      response.getWriter().write("Logout Successful");
                    }))
        .build();
  }

  @Bean
  public UserDetailsService users() {
    UserDetails user =
        User.builder().username("user").password("{noop}123123").roles("USER").build();
    UserDetails admin =
        User.builder().username("admin").password("{noop}123123").roles("ADMIN").build();
    return new InMemoryUserDetailsManager(user, admin);
  }
}
