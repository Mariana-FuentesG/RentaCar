package com.prueba.ms_reportes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO extends RepresentationModel<ReporteResponseDTO> {

    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer totalReservas;
    private Double totalIngresos;
    private Boolean activo;
    private LocalDate fechaGeneracion;
}
