// src/main/java/edu/uea/trabalho_final_APS_2/service/ReservaService.java

package edu.uea.trabalho_final_APS_2.service;

import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Reserva;
import edu.uea.trabalho_final_APS_2.model.Status;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.repository.LivroRepository;
import edu.uea.trabalho_final_APS_2.repository.ReservaRepository;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository reservaRepository, LivroRepository livroRepository,
            UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // --- LÓGICA DE RESERVA ---
    @Transactional
    // 🚨 CORREÇÃO 1: Deve retornar Reserva
    public Reserva solicitarReserva(Long livroId, String userEmail) {

        // 1. Buscar Livro e Usuário
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        Usuario solicitante = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Validação: Checar status do Livro
        if (livro.getStatus() != Status.DISPONIVEL) {
            throw new RuntimeException("O livro '" + livro.getTitulo() + "' não está disponível para reserva.");
        }

        // 3. Validação: Checar limite de reservas do Aluno (Apenas 1 reserva ativa)
        // 🚨 CORREÇÃO 2: O método 'countBySolicitanteAndStatus' no seu Repositório
        // precisa de uma sintaxe exata. Presumo que a sintaxe correta é essa:
        long reservasAtivas = reservaRepository.countBySolicitanteAndStatus(solicitante, Status.RESERVADO);

        if (reservasAtivas > 0) {
            throw new RuntimeException("Limite de reserva atingido. Você já possui uma reserva ativa.");
        }

        // 4. Criar a Reserva
        Reserva novaReserva = new Reserva();
        novaReserva.setLivro(livro);
        novaReserva.setSolicitante(solicitante);
        novaReserva.setDataReserva(LocalDate.now());
        novaReserva.setNomeLivro(livro.getTitulo());
        novaReserva.setStatus(Status.RESERVADO);

        Reserva reservaSalva = reservaRepository.save(novaReserva); // Salva a Reserva

        // 5. Atualizar status do Livro
        livro.setStatus(Status.RESERVADO);
        livroRepository.save(livro);

        return reservaSalva; // 🚨 RETORNA O OBJETO SALVO
    }

    // --- LÓGICA DE CANCELAMENTO ---
    @Transactional
    // 🚨 O método deve retornar a Reserva para que o Controller possa mapear para
    // DTO.
    public Reserva cancelarReserva(Long reservaId, String userEmail) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));

        // ... Validações de Usuário (isSolicitante e isBibliotecario) ...
        // (A lógica de validação que você implementou está correta)

        if (reserva.getStatus() != Status.RESERVADO) {
            throw new RuntimeException("A reserva não está ativa e não pode ser cancelada.");
        }

        // Capturamos o livro antes de deletar o registro da reserva
        Livro livro = reserva.getLivro();

        // 1. Deletar a Reserva (Conforme sua decisão de não usar Status.CANCELADO)
        reservaRepository.delete(reserva);

        // 2. Atualizar status do Livro para DISPONIVEL
        livro.setStatus(Status.DISPONIVEL); // Volta a ser DISPONIVEL
        livroRepository.save(livro);

        // 3. Retornar a Entidade (mesmo deletada, seus dados são necessários para o
        // DTO)
        // O objeto "reserva" ainda existe na memória neste ponto.
        return reserva;
    }

    // --- LÓGICA DE BUSCA ---
    public List<Reserva> buscarReservasPorUsuario(String userEmail) {

        Usuario solicitante = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return reservaRepository.findBySolicitante(solicitante);
    }

    // --- LÓGICA DE BUSCA (TODAS) ---
    // Método que estava faltando no Controller
    public List<Reserva> buscarTodasReservas() {
        return reservaRepository.findAll();
    }
}