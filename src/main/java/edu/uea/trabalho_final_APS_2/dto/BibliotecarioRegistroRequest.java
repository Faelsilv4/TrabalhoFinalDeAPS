// src/main/java/edu/uea/trabalho_final_APS_2/dto/BibliotecarioRegistroRequest.java

package edu.uea.trabalho_final_APS_2.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BibliotecarioRegistroRequest extends RegistroRequest {
    
    // Apenas o campo específico do Bibliotecário
    private LocalDate anoDeContratacao;
}