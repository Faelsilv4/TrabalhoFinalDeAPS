package edu.uea.trabalho_final_APS_2.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Anotações do Lombok para reduzir código repetitivo (getters/setters, construtor, etc.)
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Define que esta classe é uma Entidade JPA, mapeada para uma tabela no DB
@Entity
// (Opcional) Define o nome da tabela no banco de dados. Se omitido, usa o nome
// da classe.
@Table(name = "livros")
@Data // Gera Getters e Setters para todos os campos, toString(), equals() e
      // hashCode()
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos
public class Livro {

    // --- Atributos de Livro ---

    // Define que este campo é a Chave Primária (Primary Key) da tabela
    @Id
    // Configura como o valor da PK será gerado (IDENTITY é comum para
    // auto-incremento)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Usamos Long para IDs em JPA, que são mais robustos que Integer

    private String titulo;

    private String autor;

    private String genero;

    private Integer numPaginas;

    // Usamos LocalDate para representar a data de publicação (sem informação de
    // tempo)
    private LocalDate anoDePublicacao;

    private String categoria;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DISPONIVEL;  // SERA QUE EU COLOCO ? PRA MOSTRAR O STATUS 

    // --- Relações ---

    // Um Livro pode ter muitos Empréstimos (histórico)
    @JsonIgnore
    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // Inicializar a lista é uma boa prática
    private List<Emprestimo> historicoEmprestimos = new ArrayList<>();

  
}