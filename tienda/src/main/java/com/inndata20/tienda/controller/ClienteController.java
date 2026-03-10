package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.service.implementacion.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")

public class ClienteController {
    @Autowired
    ClienteService clienteService;

    @GetMapping("/clientes")
    public List<ClienteEntity> readAll() {
        return clienteService.readAll();
    }
}
