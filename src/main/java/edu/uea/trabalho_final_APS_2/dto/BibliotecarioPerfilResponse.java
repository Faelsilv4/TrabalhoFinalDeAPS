// src/main/java/edu/uea/trabalho_final_APS_2/dto/BibliotecarioPerfilResponse.java

package edu.uea.trabalho_final_APS_2.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BibliotecarioPerfilResponse extends UsuarioPerfilResponse {
    private LocalDate anoDeContratacao; // Apenas ano de contratação
}