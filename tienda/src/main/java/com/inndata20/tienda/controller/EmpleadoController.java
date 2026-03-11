package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
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

    // SELECCIONAR TODOS LOS EMPLEADOS (Ahora devuelve el Response DTO)
    @GetMapping("/listar")
    public List<EmpleadoDtoResponse> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

    // BUSCAR POR ID (Ahora devuelve el Response DTO)
    @GetMapping("/buscar/{id}")
    public EmpleadoDtoResponse buscarPorId(@PathVariable Integer id){
        return empleadoService.buscarPorId(id);
    }

    // GUARDA UN NUEVO EMPLEADO (Recibe el Request DTO y devuelve el mensaje String)
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarEmpleado(@RequestBody EmpleadoDtoRequest empleado) {
        String respuesta = empleadoService.guardarEmpleado(empleado);
        return ResponseEntity.ok(respuesta);
    }

    // ACTUALIZA UN EMPLEADO EXISTENTE (Recibe el Request DTO y devuelve el mensaje String)
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarEmpleado(@PathVariable Integer id, @RequestBody EmpleadoDtoRequest empleado) {
        String respuesta = empleadoService.actualizarEmpleado(id, empleado);
        return ResponseEntity.ok(respuesta);
    }

    // ELIMINA UN EMPLEADO
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Integer id) {
        if (empleadoService.eliminarEmpleado(id)) {
            return ResponseEntity.ok("Empleado eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado");
    }

    // --- TUS METODOS JPA PERSONALIZADOS ---

    // BUSCAR POR PUESTO

    @GetMapping("/puesto/{puesto}")
    public List<EmpleadoDtoResponse> buscarPorPuesto(@PathVariable String puesto) {
        return empleadoService.buscarPorPuesto(puesto);
    }

    // BUSCAR POR NOMBRE

    @GetMapping("/nombre/{nombre}")
    public List<EmpleadoDtoResponse> buscarPorNombre(@PathVariable String nombre) {
        return empleadoService.buscarPorNombre(nombre);
    }

}