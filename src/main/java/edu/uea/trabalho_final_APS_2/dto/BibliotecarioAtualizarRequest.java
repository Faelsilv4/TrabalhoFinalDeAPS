package edu.uea.trabalho_final_APS_2.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BibliotecarioAtualizarRequest {

    private String nome;
    private String email;
    private LocalDate anoDeContratacao;
}