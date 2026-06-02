// src/main/java/edu/uea/trabalho_final_APS_2/controller/EmprestimoController.java

package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.EmprestimoResponse;
import edu.uea.trabalho_final_APS_2.dto.EmprestimoSimplificadoResponse;
import edu.uea.trabalho_final_APS_2.dto.LivroResponse;
import edu.uea.trabalho_final_APS_2.dto.UsuarioPerfilResponse;
import edu.uea.trabalho_final_APS_2.model.Emprestimo;
import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.service.EmprestimoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    // --- 1. REALIZAR EMPRÉSTIMO (Aluno) ---
    
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/emprestar/{livroId}")
    public ResponseEntity<?> realizarEmprestimo(@PathVariable Long livroId, Authentication authentication) {
        String userEmail = authentication.getName();

        try {
            Emprestimo novoEmprestimo = emprestimoService.realizarNovoEmprestimo(livroId, userEmail);
            // 🚨 Retorna o DTO mapeado
            return ResponseEntity.ok(mapearEmprestimoSimplificadoResponse(novoEmprestimo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 2. DEVOLVER EMPRÉSTIMO (Bibliotecário) ---
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
@PostMapping("/devolver/{emprestimoId}")
public ResponseEntity<?> devolverEmprestimo(@PathVariable Long emprestimoId) {
    try {
        Emprestimo emprestimoDevolvido = emprestimoService.devolverLivro(emprestimoId);
        return ResponseEntity.ok(mapearEmprestimo(emprestimoDevolvido));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    // --- 3. VISUALIZAR MEUS EMPRÉSTIMOS (Aluno) ---
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/meus")
    public ResponseEntity<List<EmprestimoSimplificadoResponse>> getMeusEmprestimos(Authentication authentication) {
        String userEmail = authentication.getName();
        List<Emprestimo> emprestimos = emprestimoService.buscarEmprestimosPorUsuario(userEmail);

        // 🚨 Mapeia a lista completa para DTOs
        List<EmprestimoSimplificadoResponse> responseList = emprestimos.stream()
                .map(this::mapearEmprestimoSimplificadoResponse
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // --- 4. VISUALIZAR TODOS OS EMPRÉSTIMOS (Bibliotecário) ---
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    @GetMapping("/todos")
    public ResponseEntity<?> getTodosEmprestimos() { // <-- A mudança está aqui
        List<Emprestimo> todos = emprestimoService.buscarTodosEmprestimos();

        // 2. Verifica se a lista está vazia
        if (todos.isEmpty()) {
            // Retorna 404 NOT FOUND com a mensagem customizada no corpo (tipo String)
            String mensagem = "Nenhum empréstimo encontrado.";
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(mensagem); // Isso retorna ResponseEntity<String>
        }

        // 3. Mapeia a lista completa para DTOs
        List<EmprestimoResponse> responseList = todos.stream()
                .map(this::mapearEmprestimo)
                .collect(Collectors.toList());

        // Retorna 200 OK com a lista de DTOs (tipo
        // ResponseEntity<List<EmprestimoResponse>>)
        return ResponseEntity.ok(responseList);
    }


    // --- MAPPER SIMPLIFICADO (para /meus, /emprestar, /devolver) ---
    private EmprestimoSimplificadoResponse mapearEmprestimoSimplificadoResponse(Emprestimo emprestimo) {
        EmprestimoSimplificadoResponse dto = new EmprestimoSimplificadoResponse();
        dto.setId(emprestimo.getId());
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataDevolucao(emprestimo.getDataDevolucao());
        dto.setNomeUsuario(emprestimo.getUsuario().getNome());
        dto.setNomeLivro(emprestimo.getLivro().getTitulo());
        dto.setStatusLivro(emprestimo.getLivro().getStatus());
        return dto;
    }
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