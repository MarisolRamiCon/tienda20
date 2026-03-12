package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.InventarioRequest;
import com.inndata20.tienda.model.InventarioResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.service.implementacion.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")

public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/inventarios")
    public List<InventarioResponse> readAll()
    {
        return inventarioService.readAll();
    }

    @GetMapping("/inventario/{id}")
    public InventarioResponse readById(@PathVariable int id){
        return inventarioService.readById(id);
    }

    @PostMapping("/inventarios")
    public MensajeStrResponse create(@RequestBody InventarioRequest inventarioRequest){
        return inventarioService.create(inventarioRequest);
    }

    @PatchMapping("/inventario/{id}")
    public MensajeStrResponse updateById(@PathVariable int id, @RequestBody InventarioRequest inventarioRequest) {
        return inventarioService.updateById(id, inventarioRequest);
    }

    @PutMapping("/inventario/{id}")
    public MensajeStrResponse deleteById(@PathVariable int id) {
        return inventarioService.deleteById(id);
    }
}
