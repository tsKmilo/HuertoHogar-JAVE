package com.example.MCore.controller;

import com.example.MCore.dto.EnvioDto;
import com.example.MCore.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EnvioControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EnvioService envioService;

    @InjectMocks
    private EnvioController envioController;

    private ObjectMapper objectMapper;

    private EnvioDto envioDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        envioDto = new EnvioDto();
        envioDto.setId(1L);
        envioDto.setPedidoId(1L);
        envioDto.setDireccion("Calle Falsa 123");
        envioDto.setEstado("PREPARACION");

        // build MockMvc with the controller that has mocks injected by Mockito
        mockMvc = MockMvcBuilders.standaloneSetup(envioController).build();
    }

    @Test
    void testProgramarEnvio() throws Exception {
        given(envioService.programarEnvio(any(EnvioDto.class))).willReturn(envioDto);

        mockMvc.perform(post("/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(envioDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(1L))
                .andExpect(jsonPath("$.estado").value("PREPARACION"));
    }
}
