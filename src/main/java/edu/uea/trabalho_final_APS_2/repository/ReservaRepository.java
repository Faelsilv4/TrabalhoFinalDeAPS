package edu.uea.trabalho_final_APS_2.repository;

import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Reserva;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.model.Status; // ⬅️ ESTA DEVE ESTAR PRESENTE
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    /**
     * Conta quantas reservas ativas um usuário possui.
     * Necessário para a regra de negócio que limita a 1 reserva por aluno.
     */
    long countBySolicitanteAndStatus(Usuario solicitante, Status status);

    // Você também pode querer:
     Optional<Reserva> findByLivro(Livro livro);
    // E o find para buscar as reservas do usuário:
    // Além disso, você precisa do find para o cancelamento:
    Optional<Reserva> findByLivroAndStatus(Livro livro, Status status);
    List<Reserva> findBySolicitante(Usuario solicitante);

    // Adicione métodos customizados se necessário, como buscar por usuário ou status.
    // List<Reserva> findBySolicitanteIdAndStatus(Long solicitanteId, Status status)
    
    
}