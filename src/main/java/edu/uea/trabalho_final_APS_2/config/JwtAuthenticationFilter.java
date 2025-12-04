package edu.uea.trabalho_final_APS_2.config;

import edu.uea.trabalho_final_APS_2.service.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Injeção de dependência via construtor
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Lista de rotas que o filtro JWT deve IGNORAR
    private static final List<String> PATHS_TO_SKIP = List.of(
            "/api/auth/**", // Login e Cadastro
            "/h2-console/**",
            "/swagger-ui/**",
            "/v3/api-docs/**");

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // =========================================================================
    // 1. CORREÇÃO: IGNORAR ROTAS PÚBLICAS
    // Este método impede que o filtro JWT seja executado para as URLs de login,
    // cadastro e H2 Console.
    // Isso resolve o erro 403 Forbidden nas rotas de autenticação.
    // =========================================================================
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Verifica se o caminho da requisição corresponde a alguma rota na lista
        // PATHS_TO_SKIP
        String requestPath = request.getServletPath();

        // O AntPathMatcher lida melhor com wildcards (**)
        return PATHS_TO_SKIP.stream()
                .anyMatch(path -> pathMatcher.match(path, requestPath));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Extrai o cabeçalho 'Authorization'
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se o cabeçalho for nulo ou não começar com "Bearer ", continua o filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o JWT (removendo o prefixo "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Extrai o email do usuário do token
        userEmail = jwtService.extractUsername(jwt);

        // 4. Se o email for válido e o usuário AINDA NÃO estiver autenticado
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Carrega os detalhes do usuário usando o nosso UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Valida o token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Cria um objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) são nulas no token
                        userDetails.getAuthorities() // Papéis (Roles) do usuário
                );

                // Adiciona detalhes da requisição (IP, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Define o usuário no SecurityContextHolder (Autentica o usuário na sessão)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Passa para o próximo filtro
        filterChain.doFilter(request, response);
    }
}