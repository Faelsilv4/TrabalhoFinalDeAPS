package edu.uea.trabalho_final_APS_2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção personalizada para representar violações de regras de negócio.
 * Mapeia para o status HTTP 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String message) {
        super(message);
    }
}