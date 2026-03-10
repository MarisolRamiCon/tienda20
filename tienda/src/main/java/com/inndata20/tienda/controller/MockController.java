package com.inndata20.tienda.controller;

import com.inndata20.tienda.feign.APIMock;
import com.inndata20.tienda.service.implementacion.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<APIMock> listarProductos() {
        return mockService.listarProductos();
    }
}