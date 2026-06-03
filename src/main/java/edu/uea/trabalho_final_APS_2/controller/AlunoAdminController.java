package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.AlunoResponse;
import edu.uea.trabalho_final_APS_2.model.Aluno;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/alunos")
@PreAuthorize("hasRole('ADMIN')")
public class AlunoAdminController {

    private final UsuarioRepository usuarioRepository;

    public AlunoAdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listarAlunos() {

        List<AlunoResponse> alunos = usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getRole() == Role.ROLE_ALUNO)
                .map(this::mapearAluno)
                .toList();

        return ResponseEntity.ok(alunos);
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<AlunoResponse> ativarAluno(@PathVariable Long id) {

        Usuario usuario = buscarUsuario(id);

        usuario.setAtivo(true);

        Usuario atualizado = usuarioRepository.save(usuario);

        return ResponseEntity.ok(mapearAluno(atualizado));
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<AlunoResponse> desativarAluno(@PathVariable Long id) {

        Usuario usuario = buscarUsuario(id);

        usuario.setAtivo(false);

        Usuario atualizado = usuarioRepository.save(usuario);

        return ResponseEntity.ok(mapearAluno(atualizado));
    }

    private Usuario buscarUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado."));
    }

    private AlunoResponse mapearAluno(Usuario usuario) {

        Integer matricula = null;

        if (usuario instanceof Aluno aluno) {
            matricula = aluno.getMatricula();
        }

        return AlunoResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role("ALUNO")
                .ativo(usuario.isAtivo())
                .matricula(matricula)
                .build();
    }
}