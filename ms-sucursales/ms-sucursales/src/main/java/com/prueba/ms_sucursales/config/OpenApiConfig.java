package com.prueba.ms_sucursales.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msSucursalesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Sucursales API")
                        .description("Gestión de sucursales y regiones de RentaCar")
                        .version("v1.0"));
    }
}
