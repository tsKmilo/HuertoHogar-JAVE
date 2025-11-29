package com.example.MCore.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.MCore.dto.ItemPedidoDto;
import com.example.MCore.dto.PedidoDto;
import com.example.MCore.model.ItemPedido;
import com.example.MCore.model.Pedido;
import com.example.MCore.model.Producto;
import com.example.MCore.repository.PedidoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CatalogoService catalogoService;

    @Transactional
    public PedidoDto crearPedido(PedidoDto pedidoDto) {
        Pedido pedido = convertToEntity(pedidoDto);

        // 1. Validar y procesar cada ítem del pedido
        for (ItemPedido item : pedido.getItems()) {
            Producto producto = catalogoService.findProductoById(item.getProductoId());

            // 2. Verificar si hay stock suficiente
            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalStateException("No hay stock suficiente para el producto: " + producto.getNombre());
            }

            // 3. Asignar el precio del producto al item del pedido
            item.setPrecioUnitario(producto.getPrecio());

            // 4. Actualizar el stock del producto
            catalogoService.updateStock(producto.getId(), item.getCantidad());
        }

        // 5. Configurar datos del pedido y guardarlo
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return convertToDto(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoDto> obtenerTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Pedido findPedidoById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con id: " + id));
    }

    // Métodos de utilidad (helpers)
    private PedidoDto convertToDto(Pedido pedido) {
        PedidoDto dto = new PedidoDto();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getClienteId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setItems(pedido.getItems().stream().map(this::convertItemToDto).collect(Collectors.toList()));
        return dto;
    }

    private ItemPedidoDto convertItemToDto(ItemPedido itemPedido) {
        ItemPedidoDto dto = new ItemPedidoDto();
        dto.setProductoId(itemPedido.getProductoId());
        dto.setCantidad(itemPedido.getCantidad());
        dto.setPrecioUnitario(itemPedido.getPrecioUnitario());
        return dto;
    }

    private Pedido convertToEntity(PedidoDto dto) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.getClienteId());
        if (dto.getItems() != null) {
            pedido.setItems(dto.getItems().stream().map(itemDto -> {
                ItemPedido item = new ItemPedido();
                item.setPedido(pedido); 
                item.setProductoId(itemDto.getProductoId());
                item.setCantidad(itemDto.getCantidad());
                return item;
            }).collect(Collectors.toList()));
        }
        return pedido;
    }
}