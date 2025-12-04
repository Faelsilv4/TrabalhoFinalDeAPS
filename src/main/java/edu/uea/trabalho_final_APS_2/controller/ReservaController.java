// src/main/java/edu/uea/trabalho_final_APS_2/controller/ReservaController.java

package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.EmprestimoResponse;
import edu.uea.trabalho_final_APS_2.dto.LivroResponse;
import edu.uea.trabalho_final_APS_2.dto.ReservaResponse;
import edu.uea.trabalho_final_APS_2.dto.UsuarioPerfilResponse;
import edu.uea.trabalho_final_APS_2.model.Emprestimo;
import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Reserva;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.service.ReservaService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    //
    private ReservaResponse mapearReserva(Reserva reserva) {
        ReservaResponse dto = new ReservaResponse();
        dto.setId(reserva.getId());
        dto.setDataReserva(reserva.getDataReserva());
        dto.setStatus(reserva.getStatus()); // O enum Status

        // Mapeamento recursivo para DTOs seguros
        dto.setSolicitante(mapearUsuarioBase(reserva.getSolicitante()));
        dto.setLivro(mapearLivro(reserva.getLivro()));

        return dto;
    }
    /**
     * Endpoint para Alunos visualizarem suas reservas.
     * Requer autenticação e a ROLE_ALUNO.
     */
    // --- 1. SOLICITAR RESERVA (Aluno) ---
    // Este endpoint só precisa retornar uma mensagem de sucesso, mas vamos retornar
    // o DTO
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/reservar/{livroId}")
    public ResponseEntity<?> solicitarReserva(@PathVariable Long livroId, Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            Reserva novaReserva = reservaService.solicitarReserva(livroId, userEmail);
            // 🚨 Retorna o DTO mapeado
            return ResponseEntity.ok(mapearReserva(novaReserva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 2. CANCELAR RESERVA (Aluno/Bibliotecário) ---
    // Permite que qualquer usuário logado cancele a própria reserva, ou o
    // Bibliotecário cancele qualquer uma.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancelar/{reservaId}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long reservaId, Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            Reserva reservaCancelada = reservaService.cancelarReserva(reservaId, userEmail);
            // 🚨 Retorna o DTO mapeado
            return ResponseEntity.ok(mapearReserva(reservaCancelada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 3. VISUALIZAR MINHAS RESERVAS (Aluno) ---
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/minhas-reservas")
    public ResponseEntity<List<ReservaResponse>> getMinhasReservas(Authentication authentication) {
        String userEmail = authentication.getName();
        List<Reserva> reservas = reservaService.buscarReservasPorUsuario(userEmail);

        // 🚨 Mapeia a lista completa para DTOs
        List<ReservaResponse> responseList = reservas.stream()
                .map(this::mapearReserva)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // --- 4. VISUALIZAR TODAS AS RESERVAS (Bibliotecário) ---
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @GetMapping("/todas-reservas")
    public ResponseEntity<List<ReservaResponse>> getTodasReservas() {
        List<Reserva> todas = reservaService.buscarTodasReservas();

        // 🚨 Mapeia a lista completa para DTOs
        List<ReservaResponse> responseList = todas.stream()
                .map(this::mapearReserva)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // 🚨 ADICIONE AQUI OS TRÊS MÉTODOS AUXILIARES:
    // mapearUsuarioBase, mapearLivro, e mapearReserva

    /* ... Implementação dos métodos auxiliares aqui ... */
    // 🚨 COLOQUE OS TRÊS MÉTODOS AUXILIARES (mapearUsuarioBase, mapearLivro e
    // mapearEmprestimo) AQUI
    // Este método deve ser criado em uma classe auxiliar ou no próprio controller
    // para mapear de Entidade para DTO.

    // Mapeia Usuario (Entidade) -> UsuarioBaseResponse (DTO)
    private UsuarioPerfilResponse mapearUsuarioBase(Usuario usuario) {
        // Para simplificação, crie um método auxiliar no PerfilController
        // ou use uma biblioteca como ModelMapper.

        // Por hora, use a lógica base para evitar a senha.
        UsuarioPerfilResponse dto = new UsuarioPerfilResponse() {
        };
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole().name());
        // Outros dados específicos serão tratados em rotas específicas (GET /perfil)
        return dto;
    }

    // Mapeia Livro (Entidade) -> LivroResponse (DTO)
    private LivroResponse mapearLivro(Livro livro) {
        LivroResponse dto = new LivroResponse();
        dto.setId(livro.getId());
        dto.setTitulo(livro.getTitulo());
        dto.setAutor(livro.getAutor());
        dto.setGenero(livro.getGenero());
        dto.setNumPaginas(livro.getNumPaginas());
        dto.setAnoDePublicacao(livro.getAnoDePublicacao());
        dto.setCategoria(livro.getCategoria());
        dto.setStatus(livro.getStatus());
        return dto;
    }

    // Mapeia Emprestimo (Entidade) -> EmprestimoResponse (DTO)
    private EmprestimoResponse mapearEmprestimo(Emprestimo emprestimo) {
        EmprestimoResponse dto = new EmprestimoResponse();
        dto.setId(emprestimo.getId());
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataDevolucao(emprestimo.getDataDevolucao());

        // Mapeamento recursivo para DTOs seguros
        dto.setUsuario(mapearUsuarioBase(emprestimo.getUsuario()));
        dto.setLivro(mapearLivro(emprestimo.getLivro()));

        return dto;
    }




}