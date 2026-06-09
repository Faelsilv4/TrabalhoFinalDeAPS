package edu.uea.trabalho_final_APS_2.dto;

import lombok.Data;

@Data
public class LivroPadraoDTO {

    private String titulo;
    private String autor;
    private String genero;
    private Integer numPaginas;
    private String anoDePublicacao;
    private String categoria;
    private String urlCapa;
}