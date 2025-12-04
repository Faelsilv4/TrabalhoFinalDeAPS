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
import java.util.stream.Collectors; // 🚨 Adicionar este import

@RestController 
@RequestMapping("/api/livros") 
public class LivroController {

    @Autowired
    private LivroService livroService;

    // 1. Rota: Criar/Atualizar Livro
    // Retorna o DTO mapeado (ou Livro para simplificar a criação/atualização)
    @PostMapping 
    @PreAuthorize("hasRole('BIBLIOTECARIO')") 
    public ResponseEntity<LivroResponse> salvarLivro(@RequestBody Livro livro) {
        Livro novoLivro = livroService.salvar(livro);
        // 🚨 Mapeia o resultado para DTO antes de retornar
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearLivro(novoLivro));
    }

    // 2. Rota: Listar Todos os Livros
    // 🚨 Tipo de Retorno alterado para List<LivroResponse>
    @GetMapping 
    @PreAuthorize("hasAnyRole('ALUNO', 'BIBLIOTECARIO')") 
    public ResponseEntity<List<LivroResponse>> buscarTodosLivros() {
        List<Livro> livros = livroService.buscarTodos();
        
        // 🚨 Converte a lista de Entidades para a lista de DTOs
        List<LivroResponse> responseList = livros.stream()
             .map(this::mapearLivro)
             .collect(Collectors.toList());
             
        return ResponseEntity.ok(responseList);
    }

    // 3. Rota: Buscar Livro por ID
    // 🚨 Tipo de Retorno alterado para LivroResponse
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALUNO', 'BIBLIOTECARIO')") 
    public ResponseEntity<LivroResponse> buscarLivroPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id)
                // 🚨 Se encontrar (map), mapeia o Livro para o LivroResponse
                .map(this::mapearLivro)
                .map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.notFound().build()); 
    }

    // 4. Rota: Deletar Livro por ID (sem alteração)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BIBLIOTECARIO')") 
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Mapper Auxiliar (Mantido) ---
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
}