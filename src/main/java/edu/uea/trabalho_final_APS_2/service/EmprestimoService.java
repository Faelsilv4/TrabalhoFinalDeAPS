package edu.uea.trabalho_final_APS_2.service;

import edu.uea.trabalho_final_APS_2.model.Emprestimo;
import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.exception.RegraNegocioException; // Troque pelo seu caminho real
import edu.uea.trabalho_final_APS_2.repository.EmprestimoRepository;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    // REGRAS DEFINIDAS:
    private static final int LIMITE_EMPRESTIMOS_ALUNO = 1; // Aluno só pode emprestar 1 livro

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository,
            LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    // Método auxiliar para aplicar a REGRA DE PERMISSÃO: Apenas Aluno pode
    // Transacionar
    private void validarPermissaoAluno(Usuario usuario) {
        if (usuario.getRole() != Role.ROLE_ALUNO) {
            throw new RegraNegocioException("Apenas ALUNOS podem realizar e devolver empréstimos (transações).");
        }
    }

    /**
     * Realiza um novo empréstimo, aplicando as regras de limite e disponibilidade.
     */
    @Transactional // Adicione @Transactional se ainda não estiver lá
    public Emprestimo realizarNovoEmprestimo(Long livroId, String userEmail) {

        // 1. Buscar entidades
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RegraNegocioException("Usuário com email " + userEmail + " não encontrado."));

        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RegraNegocioException("Livro com ID " + livroId + " não encontrado."));

        //  REGRA DE LIMITE: Limite de 1 livro para Alunos
        List<Emprestimo> emprestimosAtivos = emprestimoRepository
                .findByUsuarioIdAndDataDevolucaoIsNull(usuario.getId());

        if (emprestimosAtivos.size() >= LIMITE_EMPRESTIMOS_ALUNO) {
            throw new RegraNegocioException(
                    "Limite de empréstimos excedido. Aluno pode ter no máximo "
                            + LIMITE_EMPRESTIMOS_ALUNO + " livro emprestado.");
        }

        // 🚀 REGRA DE DISPONIBILIDADE: Livro deve estar disponível
        if (livro.getStatus() != Status.DISPONIVEL) {
            throw new RegraNegocioException("O livro não está disponível para empréstimo.");
        }

        // --- 2. Criar e Configurar o Empréstimo ---
        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setUsuario(usuario);
        novoEmprestimo.setLivro(livro);
        novoEmprestimo.setDataEmprestimo(LocalDate.now());
        novoEmprestimo.setDataDevolucao(null); // Exemplo: 15 dias
        // --- 3. Atualização de Status do Livro ---
        livro.setStatus(Status.EMPRESTADO);
        livroRepository.save(livro);

        return emprestimoRepository.save(novoEmprestimo);
    }

    /**
     * Registra a devolução de um livro.
     * 
     * @param emprestimoId ID do empréstimo a ser finalizado.
     * @param userEmail    Email do aluno logado (para checar titularidade).
     */
    @Transactional
    public Emprestimo devolverLivro(Long emprestimoId, String userEmail) {

        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RegraNegocioException("Empréstimo com ID " + emprestimoId + " não encontrado."));

        // 1. Validar titularidade (O aluno só pode devolver o que pegou)
        if (!emprestimo.getUsuario().getEmail().equals(userEmail)) {
            throw new RegraNegocioException("Você não tem permissão para devolver este livro.");
        }

        // 2. Garante que o livro não foi devolvido
        if (emprestimo.getDataDevolucao() != null) {
            throw new RegraNegocioException("Este livro já foi devolvido em: " + emprestimo.getDataDevolucao());
        }

        // --- Configuração da Devolução ---
        emprestimo.setDataDevolucao(LocalDate.now());

        // --- Atualização de Status do Livro ---
        Livro livro = emprestimo.getLivro();
        livro.setStatus(Status.DISPONIVEL);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }
    // --- LÓGICA DE BUSCA ---

    /**
     * Busca todos os empréstimos de um aluno logado.
     */
    public List<Emprestimo> buscarEmprestimosPorUsuario(String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));

        // 🚨 Você precisará deste método no seu EmprestimoRepository:
        // List<Emprestimo> findByUsuario(Usuario usuario);
        return emprestimoRepository.findByUsuario(usuario);
    }

    /**
     * Busca todos os empréstimos (para o Bibliotecário).
     */
    public List<Emprestimo> buscarTodosEmprestimos() {
        return emprestimoRepository.findAll();
    }

    
}