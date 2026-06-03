package edu.uea.trabalho_final_APS_2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlunoResponse {

    private Long id;
    private String nome;
    private String email;
    private String role;
    private Boolean ativo;
    private Integer matricula;
}