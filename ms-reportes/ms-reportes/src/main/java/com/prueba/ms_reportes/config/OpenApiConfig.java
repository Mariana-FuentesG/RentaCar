package com.prueba.ms_reportes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msReportesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Reportes API")
                        .description("Generación y consolidación de reportes de RentaCar (integra ms-pagos y ms-reservas vía Feign)")
                        .version("v1.0"));
    }
}
