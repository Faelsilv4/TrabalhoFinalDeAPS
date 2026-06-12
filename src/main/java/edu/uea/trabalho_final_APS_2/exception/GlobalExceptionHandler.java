package edu.uea.trabalho_final_APS_2.exception;

import edu.uea.trabalho_final_APS_2.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RegraNegocioException.class)
        public ResponseEntity<ErroResponse> handleRegraNegocio(RegraNegocioException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                400,
                                                "Regra de negócio violada",
                                                ex.getMessage()));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErroResponse> handleBadCredentials(BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                401,
                                                "Credenciais inválidas",
                                                "Email ou senha incorretos. Verifique seus dados e tente novamente."));
        }

        @ExceptionHandler(DisabledException.class)
        public ResponseEntity<ErroResponse> handleDisabledException(DisabledException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                401,
                                                "Usuário desativado",
                                                "Sua conta está desativada. Entre em contato com o administrador da biblioteca."));
        }

        @ExceptionHandler(LockedException.class)
        public ResponseEntity<ErroResponse> handleLockedException(LockedException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                401,
                                                "Usuário desativado",
                                                "Sua conta está desativada. Entre em contato com o administrador da biblioteca."));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErroResponse> handleAccessDenied(AccessDeniedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                403,
                                                "Acesso negado",
                                                "Você não tem permissão para realizar esta ação. Verifique seu perfil de acesso."));
        }

        @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<ErroResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                404,
                                                "Recurso não encontrado",
                                                ex.getMessage()));
        }

        @ExceptionHandler(EmailJaCadastradoException.class)
        public ResponseEntity<ErroResponse> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                409,
                                                "Conflito de dados",
                                                ex.getMessage()));
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErroResponse> handleRuntimeException(RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                400,
                                                "Erro na solicitação",
                                                ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErroResponse> handleGenerico(Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                new ErroResponse(
                                                LocalDateTime.now(),
                                                500,
                                                "Erro interno",
                                                "Ocorreu um erro inesperado. Tente novamente mais tarde."));
        }
}