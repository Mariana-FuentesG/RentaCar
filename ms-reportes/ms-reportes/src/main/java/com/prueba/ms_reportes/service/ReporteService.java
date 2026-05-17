package com.prueba.ms_reportes.service;

import com.prueba.ms_reportes.client.PagoClient;
import com.prueba.ms_reportes.client.ReservaClient;
import com.prueba.ms_reportes.dto.PagoDTO;
import com.prueba.ms_reportes.dto.ReporteDTO;
import com.prueba.ms_reportes.dto.ReservaDTO;
import com.prueba.ms_reportes.mapper.ReporteMapper;
import com.prueba.ms_reportes.model.Reporte;
import com.prueba.ms_reportes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {
    @Autowired
    ReporteRepository reporteRepository;
    @Autowired
    ReservaClient reservaClient;
    @Autowired
    PagoClient pagoClient;

    // GET → LISTAR TODOS LOS REPORTES
    public List<ReporteDTO> obtenerReportes(){
        return reporteRepository.findAll()
                .stream()
                .map(ReporteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET → OBTENER REPORTE POR ID
    public ReporteDTO obtenerReportePorId(Integer id){
        Reporte reporte = reporteRepository.findById(id)
                .orElse(null);
        if(reporte == null){return null;
        }
        return ReporteMapper.toDTO(reporte);
    }

    // POST → GUARDAR REPORTE
    public ReporteDTO guardarReporte(ReporteDTO dto){
        try{Reporte reporte = ReporteMapper.toEntity(dto);
            Reporte guardado = reporteRepository.save(reporte);
            return ReporteMapper.toDTO(guardado);
        }catch (Exception e){
            return null;
        }
    }

    // PUT → ACTUALIZAR REPORTE
    public ReporteDTO actualizarReporte(Integer id, ReporteDTO dto){
        try{Reporte reporte = reporteRepository.findById(id)
                .orElse(null);
            if(reporte == null){return null;}

            // ACTUALIZAR CAMPOS INDIVIDUALMENTE
            reporte.setTitulo(dto.getTitulo());
            reporte.setDescripcion(dto.getDescripcion());
            reporte.setTotalReservas(dto.getTotalReservas());
            reporte.setTotalIngresos(dto.getTotalIngresos());
            reporte.setActivo(dto.getActivo());
            reporte.setFechaGeneracion(dto.getFechaGeneracion());

            Reporte actualizado = reporteRepository.save(reporte);
            return ReporteMapper.toDTO(actualizado);
        }catch (Exception e){
            return null;
        }
    }

    // DELETE → ELIMINAR REPORTE
    public boolean eliminarReporte(Integer id){
        try{
            if(!reporteRepository.existsById(id)){
                return false;}
            reporteRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;}
    }

    // CONSOLIDAR RESERVAS
    public List<ReservaDTO> obtenerReservas(){
        return reservaClient.obtenerReservas();
    }

    // CONSOLIDAR PAGOS
    public List<PagoDTO> obtenerPagos(){
        return pagoClient.obtenerPagos();
    }

}
