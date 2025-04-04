package com.example.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 인가
        http
                .authorizeHttpRequests((auth) -> auth // 특정 경로에 요청 허용, 거부 처리
                        .requestMatchers("/", "/login") .permitAll() // permitAll: 로그인 필요없이 모두
                        .requestMatchers("/admin").hasRole("ADMIN") // hasRole: 특정 role
                        .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated() // 나머지 경로- authenticated: 로그인만 하면 가능
                );

        // 로그인
        http.
                formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .permitAll()
        );

        // CSRF TODO: POST 요청 시 CSRF 토큰이 있어야 허용되게 다시 처리해야 함
        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }
}
