// src/main/java/edu/uea/trabalho_final_APS_2/dto/RegistroRequest.java

package edu.uea.trabalho_final_APS_2.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {
    
    // NUNCA inclua 'id' ou 'role' aqui!
    private String nome;
    private String email;
    private String senha;
}