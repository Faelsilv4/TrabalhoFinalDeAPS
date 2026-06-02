package edu.uea.trabalho_final_APS_2.service.security;

import edu.uea.trabalho_final_APS_2.dto.AlunoRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.AuthResponse;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.LoginRequest;
import edu.uea.trabalho_final_APS_2.dto.NomeUpdateRequest;
import edu.uea.trabalho_final_APS_2.dto.RegistroResponse;
import edu.uea.trabalho_final_APS_2.exception.EmailJaCadastradoException;
import edu.uea.trabalho_final_APS_2.model.Aluno;
import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()));

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(userDetails.getUsername());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado após autenticação. Erro interno.");
        }

        Usuario usuario = optionalUser.get();

        if (!usuario.isAtivo()) {
            throw new RuntimeException("Usuário desativado. Entre em contato com o administrador.");
        }

        return gerarAuthResponse(usuario);
    }

    private AuthResponse gerarAuthResponse(Usuario usuario) {
        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .tipoUsuario(usuario.getRole().name().replace("ROLE_", ""))
                .build();
    }

    public RegistroResponse registerAluno(AlunoRegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailJaCadastradoException(request.getEmail());
        }

        Aluno novoAluno = Aluno.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.ROLE_ALUNO)
                .ativo(true)
                .matricula(request.getMatricula())
                .build();

        Aluno alunoSalvo = usuarioRepository.save(novoAluno);

        return RegistroResponse.builder()
                .id(alunoSalvo.getId())
                .nome(alunoSalvo.getNome())
                .email(alunoSalvo.getEmail())
                .mensagem("Cadastro de aluno realizado com sucesso. Faça o login para acessar.")
                .build();
    }

    public RegistroResponse registerBibliotecario(BibliotecarioRegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailJaCadastradoException(request.getEmail());
        }

        Bibliotecario novoBibliotecario = Bibliotecario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.ROLE_BIBLIOTECARIO)
                .ativo(true)
                .anoDeContratacao(request.getAnoDeContratacao())
                .build();

        Bibliotecario bibliotecarioSalvo = usuarioRepository.save(novoBibliotecario);

        return RegistroResponse.builder()
                .id(bibliotecarioSalvo.getId())
                .nome(bibliotecarioSalvo.getNome())
                .email(bibliotecarioSalvo.getEmail())
                .mensagem("Cadastro de bibliotecário realizado com sucesso. Faça o login para acessar.")
                .build();
    }

    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPerfilPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Perfil de usuário não encontrado para o email: " + email));
    }

    @Transactional
    public Usuario atualizarNome(String userEmail, NomeUpdateRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        String novoNome = request.getNovoNome();

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new RuntimeException("O nome não pode ser vazio.");
        }

        usuario.setNome(novoNome);

        return usuarioRepository.save(usuario);
    }
}