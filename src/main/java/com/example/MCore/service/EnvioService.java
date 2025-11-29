package com.example.MCore.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.MCore.dto.EnvioDto;
import com.example.MCore.model.Envio;
import com.example.MCore.model.Pedido;
import com.example.MCore.repository.EnvioRepository;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private PedidoService pedidoService;

    @Transactional
    public EnvioDto programarEnvio(EnvioDto envioDto) {
        Envio envio = convertToEntity(envioDto);

        // Validar que el pedido exista
        Pedido pedido = pedidoService.findPedidoById(envio.getPedidoId());

        envio.setEstado("PREPARACION");
        pedido.setEstado("EN_PROCESO_DE_ENVIO"); // Actualizamos el estado del pedido

        Envio envioGuardado = envioRepository.save(envio);
        return convertToDto(envioGuardado);
    }

    // Métodos de utilidad (helpers)
    private EnvioDto convertToDto(Envio envio) {
        EnvioDto dto = new EnvioDto();
        dto.setId(envio.getId());
        dto.setPedidoId(envio.getPedidoId());
        dto.setDireccion(envio.getDireccion());
        dto.setEstado(envio.getEstado());
        return dto;
    }

    private Envio convertToEntity(EnvioDto dto) {
        Envio envio = new Envio();
        envio.setPedidoId(dto.getPedidoId());
        envio.setDireccion(dto.getDireccion());
        return envio;
    }
}