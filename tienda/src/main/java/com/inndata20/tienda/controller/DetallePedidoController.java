package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.DetallePedidoRequest;
import com.inndata20.tienda.model.DetallePedidoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.service.implementacion.DetallePedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping("/detallepedidos")
    public List<DetallePedidoResponse> readAll() {
        return detallePedidoService.readAll();
    }

    @GetMapping("/detallepedido/{id}")
    public Optional<DetallePedidoResponse> readById(@PathVariable int id) {
        return detallePedidoService.readById(id);
    }

    @PatchMapping("/detallepedido/{id}")
    public MensajeStrResponse update(@PathVariable int id, @RequestBody DetallePedidoRequest detallePedido) {
        return detallePedidoService.updateById(id,detallePedido);
    }

    @PutMapping("/detallepedido/{id}")
    public MensajeStrResponse delete(@PathVariable int id) {
        return detallePedidoService.deleteById(id);
    }

    @PostMapping("/detallepedido")
    public MensajeStrResponse create(@RequestBody DetallePedidoRequest detallePedido) {
        return detallePedidoService.create(detallePedido);
    }
}
