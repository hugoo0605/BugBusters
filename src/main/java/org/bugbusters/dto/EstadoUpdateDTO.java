package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoUpdateDTO {
    private String estado;

    // 🔧 Constructor vacío requerido por Jackson
    public EstadoUpdateDTO() {
    }

    // 🔧 Constructor útil si lo quieres usar manualmente
    public EstadoUpdateDTO(String estado) {
        this.estado = estado;
    }
}