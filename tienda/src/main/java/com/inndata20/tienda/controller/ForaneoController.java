package com.inndata20.tienda.controller;

import com.inndata20.tienda.feign.ForaneoEntity;
import com.inndata20.tienda.service.implementacion.ForaneoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
