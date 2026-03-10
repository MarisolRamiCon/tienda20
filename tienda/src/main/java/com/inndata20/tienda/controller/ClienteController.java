package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.service.implementacion.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")

public class ClienteController {
    @Autowired
    ClienteService clienteService;

    @GetMapping("/clientes")
    public List<ClienteDtoResponse> readAll() {
        return clienteService.readAll();
    }

    @GetMapping("/cliente/{id}")
    public Optional<ClienteDtoResponse> readById(@PathVariable int id) {
        return clienteService.readById(id);
    }

    @PostMapping("/clientes")
    public String create(@RequestBody ClienteDtoRequest clienteDtoRequest) {
        return clienteService.create(clienteDtoRequest);
    }
}
