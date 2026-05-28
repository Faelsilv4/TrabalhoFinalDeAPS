package edu.uea.trabalho_final_APS_2.dto;

import edu.uea.trabalho_final_APS_2.model.Status;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class EmprestimoSimplificadoResponse {

    private Long id;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private String nomeUsuario;
    private String nomeLivro;
    private Status statusLivro;
}