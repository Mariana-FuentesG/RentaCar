package com.prueba.ms_empleados.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msEmpleadosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Empleados API")
                        .description("Gestión de empleados de RentaCar")
                        .version("v1.0"));
    }
}
