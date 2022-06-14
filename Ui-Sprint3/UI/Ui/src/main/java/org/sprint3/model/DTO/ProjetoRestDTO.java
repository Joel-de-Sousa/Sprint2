package org.sprint3.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProjetoRestDTO {

    @Getter
    private int codProjeto;

    @Getter
    private int codProposta;

    @Getter
    private int codEstudante;

    @Getter
    private int codOrientador;

    public ProjetoRestDTO(int codProposta, int codEstudante, int codOrientador) {
        this.codProposta = codProposta;
        this.codEstudante = codEstudante;
        this.codOrientador = codOrientador;
    }
}
