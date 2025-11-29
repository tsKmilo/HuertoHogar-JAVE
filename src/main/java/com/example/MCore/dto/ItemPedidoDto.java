package com.example.MCore.dto;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemPedidoDto {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}