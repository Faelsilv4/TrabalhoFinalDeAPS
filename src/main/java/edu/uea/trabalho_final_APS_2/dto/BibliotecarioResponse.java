package edu.uea.trabalho_final_APS_2.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecarioResponse {

    private Long id;
    private String nome;
    private String email;
    private String role;
    private boolean ativo;
    private LocalDate anoDeContratacao;
}