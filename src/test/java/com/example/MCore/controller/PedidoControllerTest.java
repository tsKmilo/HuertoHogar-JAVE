package com.example.MCore.controller;


import com.example.MCore.dto.PedidoDto;
import com.example.MCore.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private ObjectMapper objectMapper;
    private PedidoDto pedidoDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();

        pedidoDto = new PedidoDto();
        pedidoDto.setId(1L);
        pedidoDto.setClienteId(1L);
        pedidoDto.setItems(new ArrayList<>());
        pedidoDto.setEstado("PENDIENTE");
    }

    @Test
    void testCrearPedido_Success() throws Exception {
        given(pedidoService.crearPedido(any(PedidoDto.class))).willReturn(pedidoDto);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testObtenerTodos() throws Exception {
        PedidoDto pedidoDto2 = new PedidoDto();
        pedidoDto2.setId(2L);
        pedidoDto2.setClienteId(2L);
        pedidoDto2.setItems(new ArrayList<>());
        pedidoDto2.setEstado("COMPLETADO");
        
        List<PedidoDto> pedidos = Arrays.asList(pedidoDto, pedidoDto2);
        given(pedidoService.obtenerTodos()).willReturn(pedidos);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testCrearPedido_Failure() throws Exception {
        given(pedidoService.crearPedido(any(PedidoDto.class))).willThrow(new IllegalStateException("Stock no disponible"));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Stock no disponible"));
    }
}
