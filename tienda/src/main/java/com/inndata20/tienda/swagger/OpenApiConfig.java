package com.inndata20.tienda.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tienda API",
                version = "v1",
                description = "Documentacion OpenAPI generada automaticamente con springdoc-openapi."
        )
)
public class OpenApiConfig {
}

