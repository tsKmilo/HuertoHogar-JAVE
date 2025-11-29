package com.example.MCore.dto;

import lombok.Data;

@Data
public class EnvioDto {
    private Long id;
    private Long pedidoId;
    private String direccion;
    private String estado;
    private String transportadora;
    private String numeroGuia;
}