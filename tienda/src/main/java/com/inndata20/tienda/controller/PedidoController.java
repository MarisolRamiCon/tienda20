package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;
import com.inndata20.tienda.service.IPedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final IPedidoService pedidoService;

    @Autowired
    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/listar")
    public List<PedidoDtoResponse> listarPedidos() {
        log.info("REST Request: Solicitando la lista de todos los pedidos");
        return pedidoService.listarPedidos();
    }

    @GetMapping("/buscar/{id}")
    public PedidoDtoResponse buscarPorId(@PathVariable Integer id) {
        log.info("REST Request: Buscando pedido con ID: {}", id);
        return pedidoService.buscarPorId(id);
    }

    @PostMapping("/guardar")
    public MensajeDtoResponse guardarPedido(@RequestBody PedidoDtoRequest pedidoRequest) {
        log.info("REST Request: Petición para guardar un nuevo pedido del cliente ID: {}", pedidoRequest.getClienteId());
        return pedidoService.guardarPedido(pedidoRequest);
    }

    @PutMapping("/actualizar/{id}")
    public MensajeDtoResponse actualizarPedido(@PathVariable Integer id, @RequestBody PedidoDtoRequest pedidoRequest) {
        log.info("REST Request: Petición para actualizar el pedido con ID: {}", id);
        return pedidoService.actualizarPedido(id, pedidoRequest);
    }

    @DeleteMapping("/eliminar/{id}")
    public MensajeDtoResponse eliminarPedido(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el pedido con ID: {}", id);
        return pedidoService.eliminarPedido(id);

    }

    // METODOS JPA PERSONALIZADOS

    @GetMapping("/fechas")
    public List<PedidoDtoResponse> buscarPorRangoFechas(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {
        log.info("REST Request: Buscando pedidos entre {} y {}", fechaInicio, fechaFin);
        return pedidoService.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<PedidoDtoResponse> buscarPorCliente(@PathVariable Integer clienteId) {
        log.info("REST Request: Buscando pedidos del cliente con ID: {}", clienteId);
        return pedidoService.buscarPorCliente(clienteId);
    }

    // QUERYS PERSONALIZADOS

    @GetMapping("/total")
    public List<PedidoDtoResponse> buscarPorRangoTotal(
            @RequestParam Double rangoMin,
            @RequestParam Double rangoMax) {
        log.info("REST Request: Buscando pedidos con total entre {} y {}", rangoMin, rangoMax);
        return pedidoService.buscarPorRangoTotal(rangoMin, rangoMax);
    }

    @GetMapping("/cliente-activo/{clienteId}")
    public List<PedidoDtoResponse> buscarPedidosActivosPorCliente(@PathVariable Integer clienteId) {
        log.info("REST Request: Buscando pedidos activos del cliente con ID: {}", clienteId);
        return pedidoService.buscarPedidosActivosPorCliente(clienteId);
    }
}