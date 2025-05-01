package org.bugbusters.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private String estado;
    private LocalDateTime fechaCreacion;
    private String notas;
    private UUID sesionId;
    private double total;
    private Long trabajadorId;

}
