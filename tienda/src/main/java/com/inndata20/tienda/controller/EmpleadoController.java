package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Empleados")
public class EmpleadoController {

    @Autowired
    public IEmpleadoService empleadoService;

    @GetMapping("/listar")
    public List<EmpleadoEntity> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

}
