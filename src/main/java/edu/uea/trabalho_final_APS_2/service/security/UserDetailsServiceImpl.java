package edu.uea.trabalho_final_APS_2.service.security;

import edu.uea.trabalho_final_APS_2.model.Usuario;
import edu.uea.trabalho_final_APS_2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional; // Importar Optional

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // CORREÇÃO: Usa Optional<Usuario> para buscar o usuário
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);

        // Se o usuário não for encontrado, lança a exceção padronizada
        Usuario usuario = optionalUsuario.orElseThrow(
            () -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email)
        );

        // Retorna a nossa implementação personalizada
        return new UserDetailsImpl(usuario);
    }
}