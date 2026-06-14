package com.prueba.ms_clientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msClientesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Clientes API")
                        .description("Gestión de clientes y sus direcciones para RentaCar")
                        .version("v1.0"));
    }
}
