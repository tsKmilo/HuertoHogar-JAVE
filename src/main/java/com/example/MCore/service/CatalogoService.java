package com.example.MCore.service;


import com.example.MCore.dto.ProductoDto;
import com.example.MCore.model.Producto;
import com.example.MCore.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CatalogoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ProductoDto> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDto getProductoById(Long id) {
        Producto producto = findProductoById(id);
        return convertToDto(producto);
    }

    @Transactional
    public ProductoDto createProducto(ProductoDto productoDto) {
        Producto producto = convertToEntity(productoDto);
        Producto productoGuardado = productoRepository.save(producto);
        return convertToDto(productoGuardado);
    }

    @Transactional
    public ProductoDto updateProducto(Long id, ProductoDto productoDto) {
        Producto productoExistente = findProductoById(id);

        productoExistente.setNombre(productoDto.getNombre());
        productoExistente.setDescripcion(productoDto.getDescripcion());
        productoExistente.setPrecio(productoDto.getPrecio());
        productoExistente.setStock(productoDto.getStock());

        Producto productoActualizado = productoRepository.save(productoExistente);
        return convertToDto(productoActualizado);
    }

    @Transactional
    public void updateStock(Long productoId, Integer cantidad) {
        Producto producto = findProductoById(productoId);
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    public Producto findProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
    }

    private ProductoDto convertToDto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        return dto;
    }

    private Producto convertToEntity(ProductoDto dto) {
        Producto producto = new Producto();
        // No se asigna el ID porque es generado por la base de datos para nuevos productos
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        return producto;
    }
}