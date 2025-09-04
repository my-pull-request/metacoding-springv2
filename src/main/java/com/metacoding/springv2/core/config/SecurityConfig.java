package com.metacoding.springv2.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.metacoding.springv2.core.filter.JWTAuthorizationFilter;
import com.metacoding.springv2.core.util.JWTProvider;
import lombok.RequiredArgsConstructor;
import com.metacoding.springv2.core.handler.Jwt401Handler;
import com.metacoding.springv2.core.handler.Jwt403Handler;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JWTProvider jwtProvider;
    private final Jwt401Handler jwt401Handler;
    private final Jwt403Handler jwt403Handler;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(f -> f.disable());
        http.httpBasic(basic -> basic.disable());

        // H2 Console은 iframe으로 동작하므로, 이 설정이 필요합니다.
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        http.csrf(configure -> configure.disable());

        http.exceptionHandling(ex -> ex
            .authenticationEntryPoint(jwt401Handler)
            .accessDeniedHandler(jwt403Handler)
        );

 
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/boards/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/replies/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
        );

        http.addFilterBefore(new JWTAuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}