package com.inndata20.tienda.controller;

import com.inndata20.tienda.feign.ForaneoEntity;
import com.inndata20.tienda.service.implementacion.ForaneoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")

public class ForaneoController {

    private final ForaneoService foraneoService;

    public  ForaneoController(ForaneoService foraneoService) {
        this.foraneoService = foraneoService;
    }

    @GetMapping("/foraneos")
    public List<ForaneoEntity> readAll() {
        return foraneoService.readAll();
    }

    @GetMapping("/foraneo/{id}")
    public ForaneoEntity readById(@PathVariable int id) {
        return foraneoService.readById(id);
    }

    @PostMapping("/foraneo")
    public ForaneoEntity create(@RequestBody ForaneoEntity foraneoEntity) {
        return foraneoService.create(foraneoEntity);
    }

    @PutMapping("/foraneo/{id}")
    public ForaneoEntity update(@PathVariable int id, @RequestBody ForaneoEntity foraneoEntity) {
        return foraneoService.update(id, foraneoEntity);
    }

    @DeleteMapping("/foraneo/{id}")
    public void delete(@PathVariable int id){
        foraneoService.delete(id);
    }
}
