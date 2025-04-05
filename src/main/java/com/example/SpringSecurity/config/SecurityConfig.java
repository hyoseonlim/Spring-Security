package com.example.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { // 암호화 메소드
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_OWNER > ROLE_MANAGER
                ROLE_MANAGER > ROLE_USER
                """);
        /* 자동으로 ROLE_ 접두사 붙여주는 방식
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("OWNER").implies("MANAGER")
            .role("MANAGER").implies("USER")
            .build();
        */
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 인가
        http
                .authorizeHttpRequests((auth) -> auth // 특정 경로에 요청 허용, 거부 처리
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc") .permitAll() // permitAll: 로그인 필요없이 모두
                        .requestMatchers("/my/**").hasAnyRole("USER") // hasRole: 특정 role
                        .requestMatchers("/admin").hasAnyRole("ADMIN")
                        .requestMatchers("/owner").hasAnyRole("OWNER")
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

        // 세션 통제 (동일 아이디로 다중 로그인 관련 설정)
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) // 하나의 아이디에 대한 다중 로그인 허용 개수
                        .maxSessionsPreventsLogin(true)); // 다중 로그인 개수 초과 시 (true: 새로운 로그인 차단 / false: 기존 세션 하나 삭제)

        // 세션 고정 공격 보호
        http
                .sessionManagement(((session) -> session
                        .sessionFixation((sessionFixation) -> sessionFixation
                                .newSession()) // 로그인 시 세션 새로 생성 (none: 세션 정보 변경 X, changeSession: 동일 세션에 대한 id 변경)
                        )
                );


        return http.build();
    }


}
