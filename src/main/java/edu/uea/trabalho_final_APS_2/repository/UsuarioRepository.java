package edu.uea.trabalho_final_APS_2.repository;

import edu.uea.trabalho_final_APS_2.model.Role;
import edu.uea.trabalho_final_APS_2.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);
}