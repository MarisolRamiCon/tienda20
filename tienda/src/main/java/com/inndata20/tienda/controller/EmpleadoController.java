package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.service.IEmpleadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final IEmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(IEmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/listar")
    public List<EmpleadoDtoResponse> listarEmpleados() {
        log.info("REST Request: Solicitando la lista de todos los empleados");
        return empleadoService.listarEmpleados();
    }

    @GetMapping("/buscar/{id}")
    public EmpleadoDtoResponse buscarPorId(@PathVariable Integer id) {
        log.info("REST Request: Buscando empleado con ID: {}", id);
        EmpleadoDtoRequest request = new EmpleadoDtoRequest();
        request.setId(id);
        return empleadoService.buscarPorId(request);
    }

    @PostMapping("/guardar")
    public ResponseEntity<MensajeDtoResponse> guardarEmpleado(@RequestBody EmpleadoDtoRequest empleado) {
        log.info("REST Request: Petición para guardar un nuevo empleado: {}", empleado.getNombre());
        MensajeDtoResponse respuesta = empleadoService.guardarEmpleado(empleado);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<MensajeDtoResponse> actualizarEmpleado(@PathVariable Integer id, @RequestBody EmpleadoDtoRequest empleadoRequest) {
        log.info("REST Request: Petición para actualizar el empleado con ID: {}", id);
        MensajeDtoResponse respuesta = empleadoService.actualizarEmpleado(id, empleadoRequest);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/eliminar/{id}")
    public MensajeDtoResponse eliminarEmpleado(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el empleado con ID: {}", id);
        if (empleadoService.eliminarEmpleado(id)) {
            log.info("Empleado con ID: {} eliminado correctamente", id);
            return new MensajeDtoResponse("Empleado eliminado correctamente", true);
        }
        log.warn("REST Request Fallido: No se pudo eliminar, empleado con ID: {} no encontrado", id);
        return new MensajeDtoResponse("Empleado no encontrado o ya eliminado", false);
    }

    // JPA PERSONALIZADOS

    @GetMapping("/puesto/{puesto}")
    public List<EmpleadoDtoResponse> buscarPorPuesto(@PathVariable String puesto) {
        log.info("REST Request: Buscando empleados con puesto '{}'", puesto);
        return empleadoService.buscarPorPuesto(puesto);
    }

    @GetMapping("/nombre/{nombre}")
    public List<EmpleadoDtoResponse> buscarPorNombre(@PathVariable String nombre) {
        log.info("REST Request: Buscando empleados con nombre que contenga '{}'", nombre);
        return empleadoService.buscarPorNombre(nombre);
    }

    // QUERYS PERSONALIZADOS

    @GetMapping("/salario")
    public List<EmpleadoDtoResponse> buscarPorRangoSalario(
            @RequestParam BigDecimal salarioMin,
            @RequestParam BigDecimal salarioMax) {
        log.info("REST Request: Buscando empleados con salario entre {} y {}", salarioMin, salarioMax);
        return empleadoService.buscarPorRangoSalario(salarioMin,salarioMax);
    }

    @GetMapping("/fecha-contratacion")
    public List<EmpleadoDtoResponse> buscarPorFechaContratacion(
            @RequestParam LocalDate fecha) {
        log.info("REST Request: Buscando empleados contratados a partir de {}", fecha);
        return empleadoService.buscarPorFechaContratacion(fecha);
    }
}