package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.LivroResponse;
import edu.uea.trabalho_final_APS_2.model.Livro;
import edu.uea.trabalho_final_APS_2.service.LivroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<LivroResponse> salvarLivro(@RequestBody Livro livro) {
        Livro novoLivro = livroService.salvar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearLivro(novoLivro));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ALUNO', 'BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<List<LivroResponse>> buscarTodosLivros() {
        List<LivroResponse> responseList = livroService.buscarTodos()
                .stream()
                .map(this::mapearLivro)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALUNO', 'BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<LivroResponse> buscarLivroPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id)
                .map(this::mapearLivro)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<?> atualizarLivro(
            @PathVariable Long id,
            @RequestBody Livro livro) {

        try {
            Livro livroAtualizado = livroService.atualizar(id, livro);
            return ResponseEntity.ok(mapearLivro(livroAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private LivroResponse mapearLivro(Livro livro) {
        LivroResponse dto = new LivroResponse();

        dto.setId(livro.getId());
        dto.setTitulo(livro.getTitulo());
        dto.setAutor(livro.getAutor());
        dto.setGenero(livro.getGenero());
        dto.setNumPaginas(livro.getNumPaginas());
        dto.setAnoDePublicacao(livro.getAnoDePublicacao());
        dto.setCategoria(livro.getCategoria());
        dto.setUrlCapa(livro.getUrlCapa());
        dto.setStatus(livro.getStatus());

        return dto;
    }
}