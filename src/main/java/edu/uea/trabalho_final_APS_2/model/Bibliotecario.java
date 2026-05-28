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

}