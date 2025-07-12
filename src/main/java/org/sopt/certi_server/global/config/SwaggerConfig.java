package org.sopt.certi_server.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.sopt.certi_server.global.annotation.DisableSwaggerSecurity;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@OpenAPIDefinition(info = @Info(
        title = "Certi API",
        description = "Certi API 문서",
        version = "v1.0.0"))

@Configuration
public class SwaggerConfig {
    @Value("https://www.certii.shop")
    private String serverUri;

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        Server server = new Server();
        server.setUrl(serverUri);

        return new OpenAPI()
                .servers(List.of(server))
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public OperationCustomizer customizer() {
        return (operation, handlerMethod) -> {
            DisableSwaggerSecurity methodAnnotation = handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);
            if (methodAnnotation != null) {
                operation.setSecurity(Collections.emptyList());
            }
            return operation;
        };

    }
}
