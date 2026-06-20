package com.prueba.ms_pagos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI msPagosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Pagos API")
                        .description("Gestión de pagos asociados a reservas de RentaCar")
                        .version("v1"));
    }
}
