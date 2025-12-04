package edu.uea.trabalho_final_APS_2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

// Define a Entidade
@Entity
// Mapeia para a tabela unica que contem todos os tipos de usuarios
@Table(name = "usuarios", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = "email")
})
// Define a estratégia de herança como Tabela Única (SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Define a coluna que irá diferenciar os tipos de usuário (Aluno,
// Bibliotecario)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)

@Data
@SuperBuilder // Usamos SuperBuilder para construtores em hierarquia de herança
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    // --- Atributos Comuns ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    // @JsonIgnore // Garante que o campo "senha" não seja serializado para JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    // O atributo 'matricula' do diagrama está na classe Aluno,
    // e 'AnoDeContratacao' em Bibliotecario.
    // Estes campos estarão nas subclasses, mas serão persistidos na tabela
    // 'usuarios'.

    // Novo campo para o Spring Security
    @Enumerated(EnumType.STRING) // Garante que o nome da Role ('ROLE_ALUNO') seja salvo no DB
    private Role role;

}