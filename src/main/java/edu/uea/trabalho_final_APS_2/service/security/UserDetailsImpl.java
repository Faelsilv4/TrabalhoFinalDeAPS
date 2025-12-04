package edu.uea.trabalho_final_APS_2.service.security;

import edu.uea.trabalho_final_APS_2.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Implementa a interface UserDetails para ser compatível com o Spring Security
public class UserDetailsImpl implements UserDetails {

    private final Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    // Retorna a lista de autorizações/papéis (Roles) do usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte a nossa enum Role (ex: ROLE_ALUNO) para SimpleGrantedAuthority
        return Collections.singletonList(
            new SimpleGrantedAuthority(usuario.getRole().name())
        );
    }

    // Retorna a senha criptografada do usuário
    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    // Retorna o identificador único do usuário (usamos o email)
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    // Métodos de estado da conta (geralmente retornam true por padrão, a menos que você queira bloquear contas)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}