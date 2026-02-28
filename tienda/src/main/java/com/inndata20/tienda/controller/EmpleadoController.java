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

    // SELECCIONAR TODOS LOS EMPLEADOS

    @GetMapping("/listar")
    public List<EmpleadoEntity> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

}

// BUSCAR POR ID

@GetMapping("/buscar/{id}")
public EmpleadoEntity buscarPorId(@PathVariable Integer id){

    return empleadoService.buscarPorId(id);

}

// GUARDA UNA NUEVO EMPLEADO

@PostMapping("/guardar")
public EmpleadoEntity guardarEmpleado(@RequestBody EmpleadoEntity empleado) {
    return empleadoService.guardarEmpleado(empleado);
}

// ACTUALIZA UNA PERSONA EXISTENTE

@PutMapping("/actualizar/{id}")
public EmpleadoEntity actualizarEmpleado(@PathVariable Integer id, @RequestBody EmpleadoEntity empleado) {
    return empleadoService.actualizarEmpleado(id, empleado);
}