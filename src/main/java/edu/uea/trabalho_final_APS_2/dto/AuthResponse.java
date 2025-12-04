package edu.uea.trabalho_final_APS_2.dto;

import lombok.Builder;
import lombok.Data;

// Objeto para devolver o token JWT ao cliente
@Data
@Builder
public class AuthResponse {
    private String token;
    private String tipoUsuario; // Ex: ALUNO ou BIBLIOTECARIO

    // 🚨 CAMPOS QUE ESTAVAM FALTANDO:
    private String email; 
    private String nome;
}