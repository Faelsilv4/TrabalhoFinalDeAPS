// src/main/java/edu/uea/trabalho_final_APS_2/dto/ReservaResponse.java

package edu.uea.trabalho_final_APS_2.dto;

import edu.uea.trabalho_final_APS_2.model.Status;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ReservaResponse {

    private Long id;
    private LocalDate dataReserva;
    private Status status; // Reservado, Cancelado, etc.
    
    // Use o DTO para o Solicitante para NUNCA expor a senha
    private UsuarioPerfilResponse solicitante;
    
    // Use o DTO simplificado do Livro
    private LivroResponse livro;
}