package edu.uea.trabalho_final_APS_2.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// Define a Entidade
@Entity
// Define o valor que será inserido na coluna 'tipo_usuario' da tabela 'usuarios'
@DiscriminatorValue("ALUNO")
@Data
@EqualsAndHashCode(callSuper = true) // Inclui os campos da superclasse (Usuario)
@SuperBuilder
@NoArgsConstructor
public class Aluno extends Usuario {

    // --- Atributos Específicos de Aluno ---
    private Integer matricula;


    
    // Construtor para forçar a definição da role no momento da criação
    
    // public Aluno(Long id, String nome, String email, String senha, Integer matricula) {
    //     // Chama o construtor da superclasse, passando o Role.
    //     super(id, nome, email, senha, Role.ROLE_ALUNO); 
    //     this.matricula = matricula;
    // }

}