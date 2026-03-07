package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.InventarioEntity;
import com.inndata20.tienda.service.implementacion.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping

public class InventarioController {
    @Autowired
    InventarioService inventarioService;

    @GetMapping("/inventarios")
    public List<InventarioEntity> readAll()
    {
        return inventarioService.readAll();
    }
}
