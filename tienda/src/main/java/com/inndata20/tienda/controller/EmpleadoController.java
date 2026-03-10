package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final IEmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(IEmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // SELECCIONAR TODOS LOS EMPLEADOS
    @GetMapping("/listar")
    public List<EmpleadoEntity> listarEmpleados() {
        return empleadoService.listarEmpleados();
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

    // ACTUALIZA UN EMPLEADO EXISTENTE
    @PutMapping("/actualizar/{id}")
    public EmpleadoEntity actualizarEmpleado(@PathVariable Integer id, @RequestBody EmpleadoEntity empleado) {
        return empleadoService.actualizarEmpleado(id, empleado);
    }

    // ELIMINA UN EMPLEADO

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Integer id) {
        if (empleadoService.eliminarEmpleado(id)) {
            return ResponseEntity.ok("Empleado eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado");
    }

}