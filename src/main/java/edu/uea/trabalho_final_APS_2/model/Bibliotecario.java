package edu.uea.trabalho_final_APS_2.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("BIBLIOTECARIO")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Bibliotecario extends Usuario {

    // --- Atributos Específicos de Bibliotecario ---
    private LocalDate anoDeContratacao;



    // Construtor para forçar a definição da role no momento da criação

    // public Bibliotecario(Long id, String nome, String email, String senha, LocalDate anoDeContratacao) {
    //     // Chama o construtor da superclasse, passando o Role.
    //     super(id, nome, email, senha, Role.ROLE_BIBLIOTECARIO); 
    //     this.anoDeContratacao = anoDeContratacao;
    // }

}