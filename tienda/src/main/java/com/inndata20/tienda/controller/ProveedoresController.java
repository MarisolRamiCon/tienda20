package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ProveedoresEntity;

import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.model.ProveedoresRequest;
import com.inndata20.tienda.model.ProveedoresResponse;
import com.inndata20.tienda.service.implementacion.ProveedoresService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")

public class ProveedoresController {

    private final ProveedoresService proveedoresService;

    public ProveedoresController(ProveedoresService proveedoresService) {
        this.proveedoresService = proveedoresService;
    }

    @GetMapping("/proveedores")
    public List<ProveedoresResponse> readAll() {
        return proveedoresService.readAll();
    }

    @GetMapping("/proveedor/{id}")
    public ProveedoresResponse readById(@PathVariable int id) {
        return proveedoresService.readById(id);
    }

    @PostMapping("/proveedores")
    public MensajeStrResponse create(@RequestBody ProveedoresRequest proveedoresRequest) {
        return proveedoresService.create(proveedoresRequest);
    }

    @PatchMapping("/proveedor/{id}")
    public MensajeStrResponse update(@PathVariable int id, @RequestBody ProveedoresRequest proveedoresRequest) {
        return proveedoresService.updateById(id, proveedoresRequest);
    }

    @PutMapping("/proveedor/{id}")
    public MensajeStrResponse delete(@PathVariable int id) {
        return proveedoresService.deleteById(id);
    }
}
