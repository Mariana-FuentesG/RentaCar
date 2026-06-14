package com.prueba.ms_reservas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msReservasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Reservas API")
                        .description("Gestión de reservas de vehículos en RentaCar")
                        .version("v1.0"));
    }
}
