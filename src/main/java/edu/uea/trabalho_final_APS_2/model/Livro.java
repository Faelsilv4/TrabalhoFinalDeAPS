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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String autor;

    private String genero;

    private Integer numPaginas;

    private LocalDate anoDePublicacao;

    private String categoria;

    private String urlCapa;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DISPONIVEL;

    @JsonIgnore
    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Emprestimo> historicoEmprestimos = new ArrayList<>();
}