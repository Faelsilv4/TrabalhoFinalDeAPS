package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.AlunoPerfilResponse;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioPerfilResponse;
import edu.uea.trabalho_final_APS_2.dto.PerfilUpdateRequest;
import edu.uea.trabalho_final_APS_2.dto.SenhaUpdateRequest;
import edu.uea.trabalho_final_APS_2.dto.UsuarioPerfilResponse;
import edu.uea.trabalho_final_APS_2.model.Aluno;
import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.model.Usuario;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Object> visualizarPerfil(Authentication authentication) {
        String userEmail = authentication.getName();

        Usuario usuario = authService.buscarPerfilPorEmail(userEmail);
        Object responseDto = mapearParaPerfilResponse(usuario);

        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    @GetMapping("/todos-usuarios")
    public ResponseEntity<List<UsuarioPerfilResponse>> getTodosUsuarios() {

        List<Usuario> usuarios = authService.buscarTodosUsuarios();

        List<UsuarioPerfilResponse> responseList = usuarios.stream()
                .map(this::mapearParaDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> atualizarPerfil(
            Authentication authentication,
            @RequestBody PerfilUpdateRequest request) {

        String userEmail = authentication.getName();

        Usuario usuarioAtualizado = authService.atualizarPerfil(userEmail, request);
        Object responseDto = mapearParaPerfilResponse(usuarioAtualizado);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/senha")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> alterarSenha(
            Authentication authentication,
            @RequestBody SenhaUpdateRequest request) {

        String userEmail = authentication.getName();

        authService.alterarSenha(userEmail, request);

        return ResponseEntity.ok("Senha alterada com sucesso.");
    }

    private UsuarioPerfilResponse mapearParaDto(Usuario usuario) {
        if (usuario instanceof Aluno aluno) {
            AlunoPerfilResponse dto = new AlunoPerfilResponse();

            dto.setId(aluno.getId());
            dto.setNome(aluno.getNome());
            dto.setEmail(aluno.getEmail());
            dto.setRole(aluno.getRole().name());

            if (aluno.getMatricula() != null) {
                dto.setMatricula(aluno.getMatricula());
            }

            return dto;

        } else if (usuario instanceof Bibliotecario bibliotecario) {
            BibliotecarioPerfilResponse dto = new BibliotecarioPerfilResponse();

            dto.setId(bibliotecario.getId());
            dto.setNome(bibliotecario.getNome());
            dto.setEmail(bibliotecario.getEmail());
            dto.setRole(bibliotecario.getRole().name());
            dto.setAnoDeContratacao(bibliotecario.getAnoDeContratacao());

            return dto;

        } else {
            UsuarioPerfilResponse dto = new UsuarioPerfilResponse() {
            };

            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setEmail(usuario.getEmail());
            dto.setRole(usuario.getRole().name());

            return dto;
        }
    }

    private Object mapearParaPerfilResponse(Usuario usuario) {
        if (usuario instanceof Aluno aluno) {
            AlunoPerfilResponse dto = new AlunoPerfilResponse();

            dto.setId(aluno.getId());
            dto.setNome(aluno.getNome());
            dto.setEmail(aluno.getEmail());
            dto.setRole(aluno.getRole().name());
            dto.setMatricula(aluno.getMatricula());

            return dto;

        } else if (usuario instanceof Bibliotecario bibliotecario) {
            BibliotecarioPerfilResponse dto = new BibliotecarioPerfilResponse();

            dto.setId(bibliotecario.getId());
            dto.setNome(bibliotecario.getNome());
            dto.setEmail(bibliotecario.getEmail());
            dto.setRole(bibliotecario.getRole().name());
            dto.setAnoDeContratacao(bibliotecario.getAnoDeContratacao());

            return dto;
        }

        throw new RuntimeException("Tipo de usuário desconhecido.");
    }
}