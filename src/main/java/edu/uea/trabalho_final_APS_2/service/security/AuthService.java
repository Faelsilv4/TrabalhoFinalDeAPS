// src/main/java/edu/uea/trabalho_final_APS_2/service/security/AuthService.java

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
import edu.uea.trabalho_final_APS_2.model.Usuario; // 🚨 Chave para o método auxiliar
import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional; // 🚨 Adicionar este import (necessário para o método login ajustado)

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

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Realiza o login, autentica o usuário e gera o JWT.
     */
    public AuthResponse login(LoginRequest request) {
        // Tenta autenticar o usuário
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()));

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 🚨 NOVO: Busca o objeto Usuario completo para passar ao método auxiliar
        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(userDetails.getUsername());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado após autenticação. Erro interno.");
        }

        // 🚨 Usa o método auxiliar unificado
        return gerarAuthResponse(optionalUser.get());
    }

    // Método privado auxiliar para salvar com a senha criptografada (mantido)
    private Usuario saveUser(Usuario usuario) {
        if (usuario.getSenha() == null) {
            throw new IllegalArgumentException("A senha não pode ser nula no cadastro.");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // 🚨🚨 NOVO MÉTODO QUE RESOLVE O PROBLEMA DE COMPILAÇÃO 🚨🚨
    /**
     * Gera o token JWT e o objeto de resposta (AuthResponse) a partir de qualquer
     * Usuario (Aluno ou Bibliotecario).
     */
    private AuthResponse gerarAuthResponse(Usuario usuario) {

        // 1. Gera o token (usando o objeto Usuario)
        // 🚨 CORREÇÃO: Passar a entidade Usuario, que possui o método generateToken no
        // JwtService
        String jwtToken = jwtService.generateToken(usuario);

        // 2. Retorna a resposta padronizada
        return AuthResponse.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .tipoUsuario(usuario.getRole().name().replace("ROLE_", ""))
                .build();
    }

    /**
     * Rota de Cadastro de Aluno
     */
    public RegistroResponse registerAluno(AlunoRegistroRequest request) { // 🚨 Retorno alterado
        // 1. Cria a Entidade a partir do DTO e define a Role e a Senha (Mantido)
        // 🚨 NOVO: VALIDAÇÃO DE E-MAIL
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailJaCadastradoException(request.getEmail());
        }
        Aluno novoAluno = Aluno.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.ROLE_ALUNO)
                .matricula(request.getMatricula())
                .build();

        // 2. Salva o usuário
        Aluno alunoSalvo = usuarioRepository.save(novoAluno);

        // 🚨 3. Retorna a confirmação do registro SEM o token
        return RegistroResponse.builder()
                .id(alunoSalvo.getId())
                .nome(alunoSalvo.getNome())
                .email(alunoSalvo.getEmail())
                .mensagem("Cadastro de aluno realizado com sucesso. Faça o login para acessar.")
                .build();
    }

    /**
     * Rota de Cadastro de Bibliotecario
     */
    public RegistroResponse registerBibliotecario(BibliotecarioRegistroRequest request) { 
        
        // 🚨 NOVO: VALIDAÇÃO DE E-MAIL
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailJaCadastradoException(request.getEmail());
        }
        // 1. Cria a Entidade a partir do DTO e define a Role e a Senha (Mantido)
        Bibliotecario novoBibliotecario = Bibliotecario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.ROLE_BIBLIOTECARIO)
                .anoDeContratacao(request.getAnoDeContratacao())
                .build();

        // 2. Salva o usuário
        Bibliotecario bibliotecarioSalvo = usuarioRepository.save(novoBibliotecario);

        // 🚨 3. Retorna a confirmação do registro SEM o token
        return RegistroResponse.builder()
                .id(bibliotecarioSalvo.getId())
                .nome(bibliotecarioSalvo.getNome())
                .email(bibliotecarioSalvo.getEmail())
                .mensagem("Cadastro de bibliotecário realizado com sucesso. Faça o login para acessar.")
                .build();
    }

    // --- 2. CONTAR USUÁRIOS (mantido)---
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    // --- NOVO MÉTODO: BUSCAR TODOS OS USUÁRIOS (mantido)---
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca a entidade Usuario completa pelo email.
     * Usado para carregar dados do perfil do usuário logado.
     * 
     * @param email Email do usuário logado (retirado do token JWT).
     * @return Entidade Usuario (Aluno ou Bibliotecario)
     */
    public Usuario buscarPerfilPorEmail(String email) {
        // Usa o Repositório para encontrar o usuário pelo email
        return usuarioRepository.findByEmail(email)
                // Lança uma exceção se não encontrar o usuário (garantindo que não retorne
                // null)
                .orElseThrow(() -> new RuntimeException("Perfil de usuário não encontrado para o email: " + email));
    }

    /**
     * Atualiza o nome do usuário logado.
     * 
     * @param userEmail Email do usuário logado (identificador).
     * @param request   DTO contendo o novo nome.
     * @return Entidade Usuario atualizada.
     */
    @Transactional
    public Usuario atualizarNome(String userEmail, NomeUpdateRequest request) {

        // 1. Buscar o usuário
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Validação básica
        String novoNome = request.getNovoNome();
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new RuntimeException("O nome não pode ser vazio.");
        }

        // 3. Atualizar e Salvar
        usuario.setNome(novoNome);

        // Retorna a entidade salva, que será mapeada no Controller
        return usuarioRepository.save(usuario);
    }
}