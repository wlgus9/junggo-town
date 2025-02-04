package com.junggotown.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        // Bearer Token 방식으로 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // SecurityRequirement 추가 (Swagger에서 Bearer 인증을 요구)
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))  // Bearer 인증을 "bearerAuth"로 등록
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("Junggo Town API")
                        .description("Junggo Town API reference for developers")
                        .version("1.0")
                );
    }
}
