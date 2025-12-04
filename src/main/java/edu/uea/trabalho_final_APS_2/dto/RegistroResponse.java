// src/main/java/edu/uea/trabalho_final_APS_2/dto/RegistroResponse.java

package edu.uea.trabalho_final_APS_2.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroResponse {
    
    private Long id;
    private String nome;
    private String email;
    private String mensagem;
}