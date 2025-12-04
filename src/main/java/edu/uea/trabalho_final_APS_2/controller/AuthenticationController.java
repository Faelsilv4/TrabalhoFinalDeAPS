package edu.uea.trabalho_final_APS_2.controller;

import edu.uea.trabalho_final_APS_2.dto.AlunoRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.AuthResponse;
import edu.uea.trabalho_final_APS_2.dto.BibliotecarioRegistroRequest;
import edu.uea.trabalho_final_APS_2.dto.LoginRequest;
import edu.uea.trabalho_final_APS_2.dto.RegistroResponse;
import edu.uea.trabalho_final_APS_2.service.security.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Rota de Login (Autentica e retorna o JWT)
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> registerAluno(@RequestBody LoginRequest request) {
        // Se a autenticação falhar, o AuthService lançará uma exceção (que vira 401/403)
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Rota de Cadastro de Aluno
     * POST /api/auth/register/aluno
     */
   // --- REGISTRO DE ALUNO ---
    @PostMapping("/register/aluno")
    public ResponseEntity<RegistroResponse> registerAluno(@RequestBody AlunoRegistroRequest request) {
        RegistroResponse response = authService.registerAluno(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Rota de Cadastro de Bibliotecario
     * POST /api/auth/register/bibliotecario
     */
    // --- REGISTRO DE BIBLIOTECARIO ---
    @PostMapping("/register/bibliotecario")
    public ResponseEntity<RegistroResponse> registerBibliotecario(@RequestBody BibliotecarioRegistroRequest request) {
        RegistroResponse response = authService.registerBibliotecario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}