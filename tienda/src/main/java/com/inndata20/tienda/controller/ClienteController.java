package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.service.implementacion.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

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

    @PatchMapping("/cliente/{id}")
    public String update(@PathVariable int id, @RequestBody ClienteDtoRequest clienteDtoRequest) {
        return clienteService.updateById(id, clienteDtoRequest);
    }

    @GetMapping("/buscarcliente")
    public List<ClienteDtoResponse> searchByNombre(@RequestParam String busqueda) {
        return clienteService.searchByName(busqueda);
    }

    @PutMapping("/cliente/{id}")
    public String delete(@PathVariable int id) {
        return clienteService.deleteById(id);
    }
}
