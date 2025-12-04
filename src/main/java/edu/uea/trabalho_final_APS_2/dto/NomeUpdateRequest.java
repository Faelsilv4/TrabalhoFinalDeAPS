// src/main/java/edu/uea/trabalho_final_APS_2/dto/NomeUpdateRequest.java

package edu.uea.trabalho_final_APS_2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NomeUpdateRequest {
    
    // O usuário só pode mudar o nome
    private String novoNome;
}