
package edu.uea.trabalho_final_APS_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalLivros;
    private long livrosDisponiveis;
    private long livrosEmprestados;
    private long totalEmprestimos;

    private long totalAlunos;
    private long alunosAtivos;
    private long alunosInativos;

    private long totalBibliotecarios;
    private long bibliotecariosAtivos;
    private long bibliotecariosInativos;
}