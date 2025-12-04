package edu.uea.trabalho_final_APS_2.dto;

import lombok.Data;

// Objeto para receber dados do POST de login
@Data
public class LoginRequest {
    private String email;
    private String senha;
}