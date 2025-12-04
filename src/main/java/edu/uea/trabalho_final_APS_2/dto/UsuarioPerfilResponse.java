package edu.uea.trabalho_final_APS_2.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilResponse {
    
    private Long id;
    private String nome;
    private String email;
    private String role;
    
    // Matrícula é um campo específico do Aluno, mas pode ser incluído 
    // ou você pode criar um DTO específico para Aluno se houver muitos campos.
}