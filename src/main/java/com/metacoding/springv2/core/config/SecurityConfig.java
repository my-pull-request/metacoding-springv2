package com.metacoding.springv2.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // H2 Console은 iframe으로 동작하므로, 이 설정이 필요합니다.
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        http.csrf(configure -> configure.disable());

        http.formLogin(form -> form
                .loginPage("/login-form")
                .loginProcessingUrl("/login") 
                .defaultSuccessUrl("/")

        );

        // /main 인증이 필요해, /admin ADMIN 권한이 필요해, /user USER 권한이 필요해
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/boards/**").hasRole("USER")
                        .requestMatchers("/api/replies/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
        );

        return http.build();
    }
}