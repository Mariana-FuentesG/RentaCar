package com.prueba.ms_reservas.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice // intercepta errores de todos los Controllers
public class GlobalExceptionHandler {
    // Captura errores de validacion @Valid -> responde 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(error); // 400
    }
    // Recurso no encontrado -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }
    //Conflicto asociación de datos -> responde 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("No se puede eliminar el estado porque tiene reservas asociadas");
    }

    // Captura cualquier otro error inesperado -> responde 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        return ResponseEntity.internalServerError()
                .body("Error interno: " + ex.getMessage()); // 500
    }
}
