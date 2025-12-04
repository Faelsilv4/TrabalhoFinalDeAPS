package edu.uea.trabalho_final_APS_2.exception; // Crie este pacote se não existir

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Mapeia para 409 Conflict
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado.");
    }
}