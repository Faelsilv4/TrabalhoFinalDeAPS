package edu.uea.trabalho_final_APS_2.dto;

import lombok.Data;

@Data
public class AlunoAtualizarRequest {

    private String nome;
    private String email;
    private Integer matricula;
}