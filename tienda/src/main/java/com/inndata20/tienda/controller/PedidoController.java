package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;
import com.inndata20.tienda.service.IPedidoService;
import lombok.extern.slf4j.Slf4j;
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

    // A partir de Spring 4.3 ya no es obligatorio poner @Autowired si solo hay un constructor,
    // pero lo dejamos porque es buena práctica de lectura.
    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // GET a /api/v1/pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDtoResponse>> listarPedidos() {
        log.info("REST Request: Solicitando la lista de todos los pedidos");
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    // GET a /api/v1/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDtoResponse> buscarPorId(@PathVariable Integer id) {
        log.info("REST Request: Buscando pedido con ID: {}", id);
        PedidoDtoResponse pedido = pedidoService.buscarPorId(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no existe
        }
        return ResponseEntity.ok(pedido); // Retorna 200 OK si lo encuentra
    }

    // POST a /api/v1/pedidos
    @PostMapping
    public ResponseEntity<MensajeDtoResponse> guardarPedido(@RequestBody PedidoDtoRequest pedidoRequest) {
        log.info("REST Request: Petición para guardar un nuevo pedido del cliente ID: {}", pedidoRequest.getClienteId());
        MensajeDtoResponse response = pedidoService.guardarPedido(pedidoRequest);

        if (response.getExito()) { // Asumo que MensajeDtoResponse tiene un boolean de éxito, si no, lo ajustamos
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // Retorna 201 Created
        }
        return ResponseEntity.badRequest().body(response); // Retorna 400 Bad Request si falló la validación (ej. no existe cliente)
    }

    // PUT a /api/v1/pedidos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDtoResponse> actualizarPedido(@PathVariable Integer id, @RequestBody PedidoDtoRequest pedidoRequest) {
        log.info("REST Request: Petición para actualizar el pedido con ID: {}", id);
        MensajeDtoResponse response = pedidoService.actualizarPedido(id, pedidoRequest);

        if (response.getExito()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // DELETE a /api/v1/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDtoResponse> eliminarPedido(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el pedido con ID: {}", id);
        MensajeDtoResponse response = pedidoService.eliminarPedido(id);

        if (response.getExito()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // METODOS JPA PERSONALIZADOS

    @GetMapping("/fechas")
    public ResponseEntity<List<PedidoDtoResponse>> buscarPorRangoFechas(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {
        log.info("REST Request: Buscando pedidos entre {} y {}", fechaInicio, fechaFin);
        return ResponseEntity.ok(pedidoService.buscarPorRangoFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDtoResponse>> buscarPorCliente(@PathVariable Integer clienteId) {
        log.info("REST Request: Buscando pedidos del cliente con ID: {}", clienteId);
        return ResponseEntity.ok(pedidoService.buscarPorCliente(clienteId));
    }

    // QUERYS PERSONALIZADOS

    @GetMapping("/total")
    public ResponseEntity<List<PedidoDtoResponse>> buscarPorRangoTotal(
            @RequestParam Double rangoMin,
            @RequestParam Double rangoMax) {
        log.info("REST Request: Buscando pedidos con total entre {} y {}", rangoMin, rangoMax);
        return ResponseEntity.ok(pedidoService.buscarPorRangoTotal(rangoMin, rangoMax));
    }

    @GetMapping("/cliente/{clienteId}/activos") // Ajusté un poco la ruta para que sea más RESTful
    public ResponseEntity<List<PedidoDtoResponse>> buscarPedidosActivosPorCliente(@PathVariable Integer clienteId) {
        log.info("REST Request: Buscando pedidos activos del cliente con ID: {}", clienteId);
        return ResponseEntity.ok(pedidoService.buscarPedidosActivosPorCliente(clienteId));
    }
}