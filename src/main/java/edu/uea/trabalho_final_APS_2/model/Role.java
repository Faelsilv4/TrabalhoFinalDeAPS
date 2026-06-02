package edu.uea.trabalho_final_APS_2.model;

public enum Role {
    
    // As roles precisam do prefixo "ROLE_" para serem reconhecidas pelo Spring Security, 
    // embora você possa configurar isso para ser diferente.
    ROLE_ALUNO, 
    ROLE_BIBLIOTECARIO,
    ROLE_ADMIN
}