// src/main/java/edu/uea/trabalho_final_APS_2/controller/PerfilController.java

package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.AlunoPerfilResponse;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioPerfilResponse;
import edu.uea.trabalho_final_APS_2.dto.NomeUpdateRequest;
import edu.uea.trabalho_final_APS_2.dto.UsuarioPerfilResponse;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.model.Aluno; // Necessário para acessar a matrícula
import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.service.security.AuthService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final AuthService authService;

    public PerfilController(AuthService authService) {
        this.authService = authService;
    }

    // --- VISUALIZAR PERFIL ---
    /**
     * O usuário logado visualiza seu próprio perfil.
     * Retorna o DTO específico (AlunoPerfilResponse ou
     * BibliotecarioPerfilResponse).
     * Rota: GET /api/perfil
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Object> visualizarPerfil(Authentication authentication) { // Retorna Object
        String userEmail = authentication.getName();
        try {
            Usuario usuario = authService.buscarPerfilPorEmail(userEmail);

            Object responseDto;

            // 🚨 LÓGICA DE MAPEAR PARA O DTO ESPECÍFICO
            if (usuario instanceof Aluno) {
                Aluno aluno = (Aluno) usuario;
                AlunoPerfilResponse dto = new AlunoPerfilResponse();

                // Mapeamento de campos base
                dto.setId(aluno.getId());
                dto.setNome(aluno.getNome());
                dto.setEmail(aluno.getEmail());
                dto.setRole(aluno.getRole().name());

                // Mapeamento de campo específico (Matrícula)
                dto.setMatricula(aluno.getMatricula());

                responseDto = dto;

            } else if (usuario instanceof Bibliotecario) {
                Bibliotecario bibliotecario = (Bibliotecario) usuario;
                BibliotecarioPerfilResponse dto = new BibliotecarioPerfilResponse();

                // Mapeamento de campos base
                dto.setId(bibliotecario.getId());
                dto.setNome(bibliotecario.getNome());
                dto.setEmail(bibliotecario.getEmail());
                dto.setRole(bibliotecario.getRole().name());

                // Mapeamento de campo específico (Ano de Contratação)
                dto.setAnoDeContratacao(bibliotecario.getAnoDeContratacao());

                responseDto = dto;

            } else {
                return ResponseEntity.internalServerError().body("Tipo de usuário desconhecido.");
            }

            return ResponseEntity.ok(responseDto);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Contagem de Usuários (APENAS LISTA) ---
    /**
     * Retorna a lista de todos os usuários cadastrados (alunos e bibliotecários).
     * Rota: GET /api/perfil/todos-usuarios
     */
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    @GetMapping("/todos-usuarios")
    public ResponseEntity<List<UsuarioPerfilResponse>> getTodosUsuarios() {

        List<Usuario> usuarios = authService.buscarTodosUsuarios();

        // 🚨 AQUI ESTÁ A SOLUÇÃO: Passar o método mapearParaDto como referência
        List<UsuarioPerfilResponse> responseList = usuarios.stream()
                .map(this::mapearParaDto) // Usa a função auxiliar
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    private UsuarioPerfilResponse mapearParaDto(Usuario usuario) {
        if (usuario instanceof Aluno) {
            Aluno aluno = (Aluno) usuario;
            AlunoPerfilResponse dto = new AlunoPerfilResponse();

            // Mapeamento Base
            dto.setId(aluno.getId());
            dto.setNome(aluno.getNome());
            dto.setEmail(aluno.getEmail());
            dto.setRole(aluno.getRole().name());

            // Específico
            if (aluno.getMatricula() != null) {
                dto.setMatricula(aluno.getMatricula());
            }
            return dto;

        } else if (usuario instanceof Bibliotecario) {
            Bibliotecario bibliotecario = (Bibliotecario) usuario;
            BibliotecarioPerfilResponse dto = new BibliotecarioPerfilResponse();

            // Mapeamento Base
            dto.setId(bibliotecario.getId());
            dto.setNome(bibliotecario.getNome());
            dto.setEmail(bibliotecario.getEmail());
            dto.setRole(bibliotecario.getRole().name());

            // Específico
            dto.setAnoDeContratacao(bibliotecario.getAnoDeContratacao());
            return dto;

        } else {
            // Tipo base, apenas para fallback
            UsuarioPerfilResponse dto = new UsuarioPerfilResponse() {
            };
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setEmail(usuario.getEmail());
            dto.setRole(usuario.getRole().name());
            return dto;
        }
    }

    // ... (método visualizarPerfil) ...

    // --- ATUALIZAR NOME ---
    /**
     * Atualiza o nome do usuário logado.
     * Retorna o DTO de perfil específico atualizado.
     */
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> atualizarNome(
            Authentication authentication,
            @RequestBody NomeUpdateRequest request) {

        String userEmail = authentication.getName();

        try {
            // 1. Chama o serviço para atualizar o nome
            Usuario usuarioAtualizado = authService.atualizarNome(userEmail, request);

            // 2. Mapeia a entidade atualizada para o DTO de Perfil específico
            Object responseDto = mapearParaPerfilResponse(usuarioAtualizado);

            return ResponseEntity.ok(responseDto);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obs: O método mapearParaPerfilResponse(Usuario usuario) deve ser o mesmo
    // que você já usa no método GET /perfil.
    private Object mapearParaPerfilResponse(Usuario usuario) {
        // Sua lógica existente de if (usuario instanceof Aluno) { ... }
        // e else if (usuario instanceof Bibliotecario) { ... }

        // ... (aqui deve vir a lógica que você usa no GET para mapear para
        // AlunoPerfilResponse ou BibliotecarioPerfilResponse)

        if (usuario instanceof Aluno) {
            Aluno aluno = (Aluno) usuario;
            AlunoPerfilResponse dto = new AlunoPerfilResponse();
            // Mapeamento de campos base
            dto.setId(aluno.getId());
            dto.setNome(aluno.getNome()); // 🚨 NOVO NOME AQUI
            dto.setEmail(aluno.getEmail());
            dto.setRole(aluno.getRole().name());
            // Mapeamento de campos específicos
            dto.setMatricula(aluno.getMatricula());
            return dto;
        } else if (usuario instanceof Bibliotecario) {
            Bibliotecario bibliotecario = (Bibliotecario) usuario;
            // ... Mapeamento para BibliotecarioPerfilResponse ...
            BibliotecarioPerfilResponse dto = new BibliotecarioPerfilResponse();
            // Mapeamento de campos base
            dto.setId(bibliotecario.getId());
            dto.setNome(bibliotecario.getNome()); // 🚨 NOVO NOME AQUI
            dto.setEmail(bibliotecario.getEmail());
            dto.setRole(bibliotecario.getRole().name());
            // Mapeamento de campos específicos
            dto.setAnoDeContratacao(bibliotecario.getAnoDeContratacao());
            return dto;
        }
        return null; // ou throw new RuntimeException("Tipo de usuário desconhecido.");
    }
}