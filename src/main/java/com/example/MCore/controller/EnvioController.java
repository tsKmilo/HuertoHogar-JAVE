package com.example.MCore.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MCore.dto.EnvioDto;
import com.example.MCore.service.EnvioService;


@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    public ResponseEntity<EnvioDto> programarEnvio(@Valid @RequestBody EnvioDto envioDto) {
        return ResponseEntity.ok(envioService.programarEnvio(envioDto));
    }
}