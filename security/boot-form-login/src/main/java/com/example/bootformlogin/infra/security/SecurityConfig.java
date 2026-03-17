package com.example.bootformlogin.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain oneSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/public/**", "/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/api/login")
                    .defaultSuccessUrl("/login-sucess", true))
        .logout(logout -> logout.logoutUrl("/api/logout").logoutSuccessUrl("/login?logout"))
        .build();
  }

  @Bean
  public UserDetailsService users() {
    UserDetails user =
        User.builder()
            .username("samwisewell")
            .password("{noop}*demopurpose#")
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }
}
