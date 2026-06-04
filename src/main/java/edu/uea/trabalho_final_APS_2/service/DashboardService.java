package edu.uea.trabalho_final_APS_2.service;

import edu.uea.trabalho_final_APS_2.dto.DashboardResponse;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.repository.EmprestimoRepository;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardService(
            LivroRepository livroRepository,
            EmprestimoRepository emprestimoRepository,
            UsuarioRepository usuarioRepository) {

        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
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

        long totalAlunos = usuarioRepository.countByRole(Role.ROLE_ALUNO);

        long alunosAtivos = usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getRole() == Role.ROLE_ALUNO)
                .filter(usuario -> usuario.isAtivo())
                .count();

        long alunosInativos = usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getRole() == Role.ROLE_ALUNO)
                .filter(usuario -> !usuario.isAtivo())
                .count();

        long totalBibliotecarios = usuarioRepository.countByRole(Role.ROLE_BIBLIOTECARIO)
                + usuarioRepository.countByRole(Role.ROLE_ADMIN);

        long bibliotecariosAtivos = usuarioRepository.findAll()
                .stream()
                .filter(usuario ->
                        usuario.getRole() == Role.ROLE_BIBLIOTECARIO ||
                        usuario.getRole() == Role.ROLE_ADMIN)
                .filter(usuario -> usuario.isAtivo())
                .count();

        long bibliotecariosInativos = usuarioRepository.findAll()
                .stream()
                .filter(usuario ->
                        usuario.getRole() == Role.ROLE_BIBLIOTECARIO ||
                        usuario.getRole() == Role.ROLE_ADMIN)
                .filter(usuario -> !usuario.isAtivo())
                .count();

        return new DashboardResponse(
                totalLivros,
                livrosDisponiveis,
                livrosEmprestados,
                totalEmprestimos,
                totalAlunos,
                alunosAtivos,
                alunosInativos,
                totalBibliotecarios,
                bibliotecariosAtivos,
                bibliotecariosInativos
        );
    }
}