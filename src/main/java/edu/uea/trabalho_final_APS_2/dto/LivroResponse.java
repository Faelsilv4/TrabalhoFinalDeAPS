package edu.uea.trabalho_final_APS_2.dto;

import java.time.LocalDate;

import edu.uea.trabalho_final_APS_2.model.Status;
import lombok.Data;

@Data
public class LivroResponse {

    private Long id;
    private String titulo;
    private String autor;
    private String genero;
    private Integer numPaginas;
    private LocalDate anoDePublicacao;
    private String categoria;
    private String urlCapa;
    private Status status;
}