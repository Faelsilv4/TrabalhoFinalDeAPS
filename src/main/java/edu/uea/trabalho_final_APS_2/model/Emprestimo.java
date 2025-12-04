package edu.uea.trabalho_final_APS_2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "emprestimos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {

    // --- Atributos Próprios de Emprestimo ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataEmprestimo;


    private LocalDate dataDevolucao;

    //private Integer qtdExemplares;
    
    // --- Relação Muitos-Para-Um (Many-to-One) com Usuario ---
    // Um Emprestimo é realizado por UM Usuario.
    @ManyToOne 
    @JoinColumn(name = "usuario_id", nullable = false) // Chave Estrangeira (FK)
    private Usuario usuario; // Nome da variável conforme a associação 'emprestimo' no diagrama

    // --- Relação Muitos-Para-Um (Many-to-One) com Livro ---
    // Um Emprestimo se refere a UM Livro.
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false) // Chave Estrangeira (FK)
    private Livro livro; // Nome da variável conforme a associação 'livros' no diagrama
}