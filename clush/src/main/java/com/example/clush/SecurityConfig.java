package com.example.clush; // 패키지 선언

import org.springframework.context.annotation.Bean; // Bean을 정의하기 위한 import
import org.springframework.context.annotation.Configuration; // 구성 클래스를 정의하기 위한 import
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HTTP 보안 설정을 위한 import
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // 웹 보안 활성화를 위한 import
import org.springframework.security.web.SecurityFilterChain; // 보안 필터 체인을 위한 import
import org.springframework.web.servlet.config.annotation.CorsRegistry; // CORS 설정을 위한 클래스
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Spring MVC 설정 인터페이스
import org.springframework.lang.NonNull;


// Spring Security 및 CORS 구성을 위한 클래스
@Configuration
@EnableWebSecurity // Spring Security 기능을 활성화
public class SecurityConfig implements WebMvcConfigurer {

    // 보안 필터 체인을 설정하는 메소드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
        .authorizeHttpRequests(authorize -> authorize // 요청에 대한 권한 설정
            .anyRequest().permitAll()) // 모든 요청은 기본적으로 접근 허용
            .httpBasic(httpBasic -> httpBasic.realmName("Realm")); // 기본 HTTP 인증 사용 및 Realm 설정

        return http.build(); // 설정 완료 후 보안 필터 체인 반환
    }

    // CORS 매핑 추가
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
                .allowedOrigins("http://localhost:3000,http://localhost:3000/register") // 모든 출처 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 인증 정보 허용
    }
}