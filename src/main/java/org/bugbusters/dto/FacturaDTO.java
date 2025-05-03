package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FacturaDTO {

    // Getters y Setters
    private Long id;
    private Long pedidoId;
    private Double total;
    private LocalDateTime fecha;
    private String estado;

}
