package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.service.implementacion.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class PedidoController {
    @Autowired
    PedidoService pedidoService;

    @GetMapping("/listar")
    public List<PedidoDtoRequest> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/buscar/{id}")
    public PedidoDtoRequest buscarPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id);
    }

    @PostMapping("/guardar")
    public PedidoEntity guardarPedido(@RequestBody PedidoDtoRequest dto) {
        return pedidoService.guardarPedido(dto);
    }

    @PutMapping("/actualizar/{id}")
    public PedidoEntity actualizarPedido(@PathVariable Integer id, @RequestBody PedidoDtoRequest dto) {
        return pedidoService.actualizarPedido(id, dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Integer id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.ok("Pedido eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
    }

}
