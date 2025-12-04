package edu.uea.trabalho_final_APS_2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    // --- Atributos Próprios de Reserva ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataReserva;

    // Mapeia a enumeração Status. 
    // Por padrão, usa EnumType.ORDINAL (os inteiros 0, 1, 2). 
    // Usamos EnumType.STRING para salvar o nome (melhor para legibilidade e manutenção).
    @Enumerated(EnumType.STRING)
    private Status status; 
    
    // O atributo 'nomeLivro' do diagrama é redundante se tivermos o relacionamento com Livro, 
    // mas se for exigido, deve ser mantido:
    private String nomeLivro; 

    // --- Relação Muitos-Para-Um (Many-to-One) com Usuario ---
    // Uma Reserva é solicitada por UM Usuario.
    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false) 
    private Usuario solicitante; // Nome conforme a label 'solicitante' no diagrama

    // --- Relação Muitos-Para-Um (Many-to-One) com Livro ---
    // Uma Reserva contém UM Livro.
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false) 
    private Livro livro; // Nome conforme a label 'contem' no diagrama
}