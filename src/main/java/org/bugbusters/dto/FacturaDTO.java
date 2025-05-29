package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class FacturaDTO {
    private Long id;
    private List<Long> pedidoIds;
    private Double total;
    private LocalDateTime fecha;
    private String estado;
}