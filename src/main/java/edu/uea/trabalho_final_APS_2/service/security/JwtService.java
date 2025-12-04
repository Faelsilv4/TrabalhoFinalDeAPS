package edu.uea.trabalho_final_APS_2.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.uea.trabalho_final_APS_2.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    // ===============================================================
    // 1. Extração de Informações (Claims)
    // ===============================================================

    /** Extrai o 'username' (email) do token. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extrai um 'Claim' específico (ex: data de expiração, assunto). */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey()) // Usa a chave secreta para decodificar
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===============================================================
    // 2. Geração do Token
    // ===============================================================

  /** Gera um token JWT para um UserDetails (usado no Login). */
    public String generateToken(UserDetails userDetails) {
        // ... (implementação original que você já tem) ...
        Map<String, Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("ROLE_DEFAULT");
        claims.put("role", role);
        return buildToken(claims, userDetails.getUsername());
    }

   // 🚨🚨 NOVO MÉTODO QUE RESOLVE O PROBLEMA DE COMPILAÇÃO 🚨🚨
    /** * Gera um token JWT para a Entidade Usuario (usado no Registro).
     * @param usuario A entidade Usuario (Aluno ou Bibliotecario).
     */
    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        
        // 1. Define a role como Claim
        claims.put("role", usuario.getRole().name()); // Usa a Role da entidade

        // 2. Usa o método buildToken para finalizar
        return buildToken(claims, usuario.getEmail());
    }

   private String buildToken(Map<String, Object> claims, String username) {
        // ... (lógica de construção do token permanece igual) ...
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username) // Assunto (Subject) é o email do usuário
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }
    // ===============================================================
    // 3. Validação do Token
    // ===============================================================

    /** Verifica se o token é válido. */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // O token é válido se:
        // 1. O username no token corresponde ao username do UserDetails
        // 2. O token ainda não expirou
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ===============================================================
    // 4. Chave de Assinatura
    // ===============================================================

    /** Converte a chave secreta de String Base64 para um objeto Key. */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

}