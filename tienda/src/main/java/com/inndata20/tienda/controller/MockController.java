package com.inndata20.tienda.controller;

import com.inndata20.tienda.feign.APIMock;
import com.inndata20.tienda.service.implementacion.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/mock")
public class MockController {

    private final MockService mockService;

    @Autowired
    public MockController(MockService mockService) {
        this.mockService = mockService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<APIMock>> listarProductos() {
        return ResponseEntity.ok(mockService.listarProductos());
    }

    //  Buscar por ID
    @GetMapping("/listar/{id}")
    public ResponseEntity<APIMock> obtenerProducto(@PathVariable String id) {
        return ResponseEntity.ok(mockService.obtenerProducto(id));
    }

    //  Crear
    @PostMapping("/crear")
    public ResponseEntity<APIMock> crearProducto(@RequestBody APIMock producto) {
        return ResponseEntity.ok(mockService.crearProducto(producto));
    }

    //  Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<APIMock> actualizarProducto(@PathVariable String id, @RequestBody APIMock producto) {
        return ResponseEntity.ok(mockService.actualizarProducto(id, producto));
    }

    //  Eliminar
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        mockService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

}