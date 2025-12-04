// src/main/java/edu/uea/trabalho_final_APS_2/dto/LivroResponse.java

package edu.uea.trabalho_final_APS_2.dto;

import edu.uea.trabalho_final_APS_2.model.Status;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class LivroResponse {
    
    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private Integer numPaginas;
    private LocalDate anoDePublicacao;
    private String categoria;
    private Status status; // Enum DISPONIVEL, EMPRESTADO, RESERVADO
    
    // NUNCA retorne a lista completa de Emprestimos ou Reservas aqui para evitar ciclos.
}