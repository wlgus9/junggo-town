package com.junggotown.config;

import com.junggotown.global.BearerInterceptor;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    // http://localhost:8080/swagger-ui/index.html


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BearerInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/members/join", "/members/login", "/members/logout"
                );
    }

    @Bean
    public OpenAPI api() {
        // Bearer Token 방식으로 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)  // HTTP 방식 사용
                .scheme("bearer")  // bearer scheme 사용
                .bearerFormat("JWT");  // JWT 형식의 토큰 사용

        // SecurityRequirement 추가 (Swagger에서 Bearer 인증을 요구)
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))  // Bearer 인증을 "bearerAuth"로 등록
                .addSecurityItem(securityRequirement)  // 모든 API에 대해 Bearer 인증 적용
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("Junggo Town API")
                .description("Junggo Town API reference for developers")
                .version("1.0");
    }
}
