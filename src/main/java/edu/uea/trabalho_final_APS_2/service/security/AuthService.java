package edu.uea.trabalho_final_APS_2.service.security;

import edu.uea.trabalho_final_APS_2.dto.AlunoRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.AuthResponse;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.LoginRequest;
import edu.uea.trabalho_final_APS_2.dto.PerfilUpdateRequest;
import edu.uea.trabalho_final_APS_2.dto.RegistroResponse;
import edu.uea.trabalho_final_APS_2.dto.SenhaUpdateRequest;
import edu.uea.trabalho_final_APS_2.exception.EmailJaCadastradoException;
import edu.uea.trabalho_final_APS_2.model.Aluno;
import edu.uea.trabalho_final_APS_2.model.Bibliotecario;
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

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
        validarCadastroAluno(request);

        String nome = request.getNome().trim();
        String email = request.getEmail().trim().toLowerCase();
        String senha = request.getSenha().trim();

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(email);
        }

        Aluno novoAluno = Aluno.builder()
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
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
        validarCadastroBasico(
                request.getNome(),
                request.getEmail(),
                request.getSenha());

        String nome = request.getNome().trim();
        String email = request.getEmail().trim().toLowerCase();
        String senha = request.getSenha().trim();

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(email);
        }

        Bibliotecario novoBibliotecario = Bibliotecario.builder()
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
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

    private void validarCadastroAluno(AlunoRegistroRequest request) {
        validarCadastroBasico(
                request.getNome(),
                request.getEmail(),
                request.getSenha());

        if (request.getMatricula() == null || request.getMatricula() <= 0) {
            throw new RuntimeException("Informe uma matrícula válida.");
        }
    }

    private void validarCadastroBasico(String nome, String email, String senha) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RuntimeException("Informe o nome.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Informe o email.");
        }

        if (!EMAIL_REGEX.matcher(email.trim()).matches()) {
            throw new RuntimeException("Informe um email válido.");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new RuntimeException("Informe a senha.");
        }

        if (senha.trim().length() < 4) {
            throw new RuntimeException("A senha deve possuir pelo menos 4 caracteres.");
        }
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
    public Usuario atualizarPerfil(String userEmail, PerfilUpdateRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome não pode ser vazio.");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("O email não pode ser vazio.");
        }

        if (!EMAIL_REGEX.matcher(request.getEmail().trim()).matches()) {
            throw new RuntimeException("Informe um email válido.");
        }

        Optional<Usuario> usuarioComEmail = usuarioRepository.findByEmail(request.getEmail().trim().toLowerCase());

        if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(usuario.getId())) {
            throw new RuntimeException("Este email já está cadastrado para outro usuário.");
        }

        usuario.setNome(request.getNome().trim());
        usuario.setEmail(request.getEmail().trim().toLowerCase());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(String userEmail, SenhaUpdateRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (request.getSenhaAtual() == null || request.getSenhaAtual().trim().isEmpty()) {
            throw new RuntimeException("Informe a senha atual.");
        }

        if (request.getNovaSenha() == null || request.getNovaSenha().trim().isEmpty()) {
            throw new RuntimeException("Informe a nova senha.");
        }

        if (request.getConfirmarNovaSenha() == null || request.getConfirmarNovaSenha().trim().isEmpty()) {
            throw new RuntimeException("Confirme a nova senha.");
        }

        String senhaAtual = request.getSenhaAtual().trim();
        String novaSenha = request.getNovaSenha().trim();
        String confirmarNovaSenha = request.getConfirmarNovaSenha().trim();

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        if (novaSenha.length() < 4) {
            throw new RuntimeException("A nova senha deve possuir pelo menos 4 caracteres.");
        }

        if (!novaSenha.equals(confirmarNovaSenha)) {
            throw new RuntimeException("A confirmação da senha não confere.");
        }

        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new RuntimeException("A nova senha deve ser diferente da senha atual.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));

        usuarioRepository.save(usuario);
    }
}