package edu.uea.trabalho_final_APS_2.service;

import edu.uea.trabalho_final_APS_2.dto.DashboardResponse;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.repository.EmprestimoRepository;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    public DashboardService(
            LivroRepository livroRepository,
            EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public DashboardResponse buscarIndicadores() {
        long totalLivros = livroRepository.count();

        long livrosDisponiveis = livroRepository.findAll()
                .stream()
                .filter(livro -> livro.getStatus() == Status.DISPONIVEL)
                .count();

        long livrosEmprestados = livroRepository.findAll()
                .stream()
                .filter(livro -> livro.getStatus() == Status.EMPRESTADO)
                .count();

        long totalEmprestimos = emprestimoRepository.count();

        return new DashboardResponse(
                totalLivros,
                livrosDisponiveis,
                livrosEmprestados,
                totalEmprestimos
        );
    }
}