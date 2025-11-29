package com.example.MCore.dto;

import lombok.Data;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private java.math.BigDecimal precio;
    private Integer stock;
}