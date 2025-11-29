package com.example.MCore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MCore.dto.ProductoDto;
import com.example.MCore.service.CatalogoService;


@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/productos")
    public List<ProductoDto> getAllProductos() {
        return catalogoService.getAllProductos();
    }

    @PostMapping("/productos")
    public ProductoDto createProducto(@RequestBody ProductoDto productoDto) {
        return catalogoService.createProducto(productoDto);
    }
    
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDto> getProductoById(@PathVariable Long id) {
    
        return ResponseEntity.ok(catalogoService.getProductoById(id));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoDto> updateProducto(@PathVariable Long id, @RequestBody ProductoDto productoDto) {
        ProductoDto productoActualizado = catalogoService.updateProducto(id, productoDto);
        return ResponseEntity.ok(productoActualizado);
    }
}