// src/main/java/edu/uea/trabalho_final_APS_2/dto/AlunoRegistroRequest.java

package edu.uea.trabalho_final_APS_2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoRegistroRequest extends RegistroRequest {
    
    // Apenas o campo específico do Aluno
    private Integer matricula;
}