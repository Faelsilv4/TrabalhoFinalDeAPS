package edu.uea.trabalho_final_APS_2.repository;

import edu.uea.trabalho_final_APS_2.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// A anotação @Repository é opcional aqui, mas é boa prática
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Com esta declaração simples, você já tem:
    // - save()
    // - findById()
    // - findAll()
    // - deleteById()
    // e muitos outros métodos...

    // Exemplo de um método customizado que o Spring Data JPA implementa automaticamente:
    // List<Livro> findByAutor(String autor);

}