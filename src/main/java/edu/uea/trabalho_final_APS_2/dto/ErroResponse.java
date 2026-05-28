package edu.uea.trabalho_final_APS_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErroResponse {

    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
}