package edu.uea.trabalho_final_APS_2.repository;

import edu.uea.trabalho_final_APS_2.model.Emprestimo;
import edu.uea.trabalho_final_APS_2.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    
    /**
     * Busca todos os empréstimos ativos de um usuário, onde a data de devolução real é NULL.
     * Este método agora é válido, pois o campo dataDevolucaoRealizada existe na entidade.
     */
    // ✅ O correto
    List<Emprestimo> findByUsuarioIdAndDataDevolucaoIsNull(Long usuarioId);
    // O Spring está lendo isso corretamente como: Emprestimo.getDataDevolucao() IS NULL
    // Método para buscar todos os empréstimos por ID de usuário (histórico)
    List<Emprestimo> findByUsuarioId(Long usuarioId);
    // 2. Busca todos os empréstimos de um usuário (para o GET /meus)
    List<Emprestimo> findByUsuario(Usuario usuario);
}