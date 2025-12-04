package edu.uea.trabalho_final_APS_2.repository;

import edu.uea.trabalho_final_APS_2.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Com esta interface, você pode persistir e buscar tanto Alunos quanto Bibliotecários.
    // O Spring Data JPA é inteligente o suficiente para retornar o tipo correto.

    // Exemplo de busca customizada que será útil para autenticação:
    // Em UsuarioRepository.java (Interface)
    Optional<Usuario> findByEmail(String email); // <-- O correto é retornar Optional
    boolean existsByEmail(String email);
    
}