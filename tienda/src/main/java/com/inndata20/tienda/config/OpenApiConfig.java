package com.inndata20.tienda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * Configuración de OpenAPI/Swagger para la API de Tienda
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Producción")
                ))
                .info(new Info()
                        .title("API Tienda")
                        .version("1.0.0")
                        .description("Documentación de la API REST de Tienda. Este proyecto proporciona endpoints para gestionar clientes, pedidos, productos, empleados, proveedores e inventario.")
                        .contact(new Contact()
                                .name("Soporte Tienda")
                                .email("soporte@tienda.com")
                                .url("https://www.tienda.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}

