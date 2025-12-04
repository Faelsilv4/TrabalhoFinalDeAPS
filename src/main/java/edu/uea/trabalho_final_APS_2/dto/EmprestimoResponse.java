// src/main/java/edu/uea/trabalho_final_APS_2/dto/EmprestimoResponse.java

package edu.uea.trabalho_final_APS_2.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class EmprestimoResponse {

    private Long id;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // Será null se o livro estiver ativo
    
    // Use DTOs seguros
    private UsuarioPerfilResponse usuario;
    private LivroResponse livro;
}