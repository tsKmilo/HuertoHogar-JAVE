package com.example.MCore.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDto {
    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private String estado;
    private List<ItemPedidoDto> items;
}