package edu.uea.trabalho_final_APS_2.dto;

import lombok.Data;

@Data
public class SenhaUpdateRequest {

    private String senhaAtual;
    private String novaSenha;
    private String confirmarNovaSenha;
}