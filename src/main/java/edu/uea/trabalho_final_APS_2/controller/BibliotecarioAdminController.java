package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.BibliotecarioAtualizarRequest;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioResponse;
import edu.uea.trabalho_final_APS_2.dto.RegistroResponse;
import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import edu.uea.trabalho_final_APS_2.service.security.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/bibliotecarios")
@PreAuthorize("hasRole('ADMIN')")
public class BibliotecarioAdminController {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    public BibliotecarioAdminController(
            UsuarioRepository usuarioRepository,
            AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<RegistroResponse> cadastrarBibliotecario(
            @RequestBody BibliotecarioRegistroRequest request) {

        RegistroResponse response = authService.registerBibliotecario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BibliotecarioResponse>> listarBibliotecarios() {
        List<BibliotecarioResponse> bibliotecarios = usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getRole() == Role.ROLE_BIBLIOTECARIO ||
                        usuario.getRole() == Role.ROLE_ADMIN)
                .map(this::mapearBibliotecario)
                .toList();

        return ResponseEntity.ok(bibliotecarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BibliotecarioResponse> atualizarBibliotecario(
            @PathVariable Long id,
            @RequestBody BibliotecarioAtualizarRequest request) {

        Usuario usuario = buscarUsuario(id);
        validarSeEhBibliotecarioOuAdmin(usuario);

        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do bibliotecário não pode ser vazio.");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("O email do bibliotecário não pode ser vazio.");
        }

        Optional<Usuario> usuarioComEmail = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
            throw new RuntimeException("Este email já está cadastrado para outro usuário.");
        }

        usuario.setNome(request.getNome().trim());
        usuario.setEmail(request.getEmail().trim());

        if (usuario instanceof Bibliotecario bibliotecario) {
            bibliotecario.setAnoDeContratacao(request.getAnoDeContratacao());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return ResponseEntity.ok(mapearBibliotecario(usuarioAtualizado));
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<BibliotecarioResponse> ativarBibliotecario(@PathVariable Long id) {
        Usuario usuario = buscarUsuario(id);

        validarSeEhBibliotecarioOuAdmin(usuario);

        usuario.setAtivo(true);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return ResponseEntity.ok(mapearBibliotecario(usuarioAtualizado));
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<BibliotecarioResponse> desativarBibliotecario(@PathVariable Long id) {
        Usuario usuario = buscarUsuario(id);

        validarSeEhBibliotecarioOuAdmin(usuario);

        if (usuario.getRole() == Role.ROLE_ADMIN) {
            throw new RuntimeException("Não é possível desativar o administrador principal do sistema.");
        }

        usuario.setAtivo(false);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return ResponseEntity.ok(mapearBibliotecario(usuarioAtualizado));
    }

    @GetMapping("/teste")
    public String teste() {
        return "OK";
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bibliotecário não encontrado."));
    }

    private void validarSeEhBibliotecarioOuAdmin(Usuario usuario) {
        if (usuario.getRole() != Role.ROLE_BIBLIOTECARIO &&
                usuario.getRole() != Role.ROLE_ADMIN) {
            throw new RuntimeException("Usuário informado não é bibliotecário.");
        }
    }

    private BibliotecarioResponse mapearBibliotecario(Usuario usuario) {
        LocalDate anoDeContratacao = null;

        if (usuario instanceof Bibliotecario bibliotecario) {
            anoDeContratacao = bibliotecario.getAnoDeContratacao();
        }

        return BibliotecarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole().name().replace("ROLE_", ""))
                .ativo(usuario.isAtivo())
                .anoDeContratacao(anoDeContratacao)
                .build();
    }
}