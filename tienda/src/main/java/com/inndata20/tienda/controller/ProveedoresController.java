package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ProveedoresEntity;

import com.inndata20.tienda.service.implementacion.ProveedoresService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")

public class ProveedoresController {

    private final ProveedoresService proveedoresService;

    public ProveedoresController(ProveedoresService proveedoresService) {
        this.proveedoresService = proveedoresService;
    }

    @GetMapping("/proveedores")
    public List<ProveedoresEntity> readAll() {
        return proveedoresService.readAll();
    }
}
