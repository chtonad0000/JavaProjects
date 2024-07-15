package com.Chub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/OwnerController/**").hasRole("ADMIN")
                        .requestMatchers("/CatController/GetCats/cats").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/CatController/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())

                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User
                        .withUsername("dan305305")
                        .password("{noop}123456789")
                        .roles("ADMIN")
                        .build();
        UserDetails admin =
                User
                        .withUsername("123")
                        .password("{noop}111")
                        .roles("USER")
                        .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}

