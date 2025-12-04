

package edu.uea.trabalho_final_APS_2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoPerfilResponse extends UsuarioPerfilResponse {
    private Integer matricula; // Apenas matrícula
}