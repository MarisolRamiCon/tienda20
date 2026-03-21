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

    // GET a /api/v1/empleados (quitamos el /listar)
    @GetMapping
    public ResponseEntity<Object> listarEmpleados() {
        log.info("REST Request: Solicitando la lista de todos los empleados");
        List<EmpleadoDtoResponse> empleados = empleadoService.listarEmpleados();

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDtoResponse("No se encontraron empleados activos", false));
        }
        return ResponseEntity.ok(empleados);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        log.info("REST Request: Buscando empleado con ID: {}", id);
        EmpleadoDtoRequest empleadoRequest = new EmpleadoDtoRequest();
        empleadoRequest.setId(id);
        EmpleadoDtoResponse empleado = empleadoService.buscarPorId(empleadoRequest);

        if (empleado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDtoResponse("Empleado con ID " + id + " no encontrado", false));
        }
        return ResponseEntity.ok(empleado);
    }

    // POST a /api/v1/empleados (quitamos el /guardar)
    @PostMapping
    public ResponseEntity<MensajeDtoResponse> guardarEmpleado(@RequestBody EmpleadoDtoRequest empleadoRequest) {
        log.info("REST Request: Petición para guardar un nuevo empleado: {}", empleadoRequest.getNombre());
        MensajeDtoResponse respuesta = empleadoService.guardarEmpleado(empleadoRequest);

        if (Boolean.TRUE.equals(respuesta.getExito())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta); // 201 Created
        }
        return ResponseEntity.badRequest().body(respuesta); // 400 Bad Request
    }

    // PUT a /api/v1/empleados/{id} (quitamos el /actualizar)
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDtoResponse> actualizarEmpleado(@PathVariable Integer id, @RequestBody EmpleadoDtoRequest empleadoRequest) {
        log.info("REST Request: Petición para actualizar el empleado con ID: {}", id);
        MensajeDtoResponse respuesta = empleadoService.actualizarEmpleado(id, empleadoRequest);

        if (Boolean.TRUE.equals(respuesta.getExito())) {
            return ResponseEntity.ok(respuesta); // 200 OK
        }
        return ResponseEntity.badRequest().body(respuesta); // 400 Bad Request
    }

    // DELETE a /api/v1/empleados/{id} (quitamos el /eliminar)
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDtoResponse> eliminarEmpleado(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el empleado con ID: {}", id);
        MensajeDtoResponse respuesta = empleadoService.eliminarEmpleado(id);

        if (Boolean.TRUE.equals(respuesta.getExito())) {
            return ResponseEntity.ok(respuesta); // 200 OK
        }
        return ResponseEntity.badRequest().body(respuesta); // 400 Bad Request
    }

    // ==========================================
    // JPA PERSONALIZADOS
    // ==========================================

    @GetMapping("/puesto/{puesto}")
    public ResponseEntity<Object> buscarPorPuesto(@PathVariable String puesto) {
        log.info("REST Request: Buscando empleados con puesto '{}'", puesto);
        List<EmpleadoDtoResponse> empleados = empleadoService.buscarPorPuesto(puesto);

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDtoResponse("No se encontraron empleados con el puesto: " + puesto, false));
        }
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Object> buscarPorNombre(@PathVariable String nombre) {
        log.info("REST Request: Buscando empleados con nombre que contenga '{}'", nombre);
        List<EmpleadoDtoResponse> empleados = empleadoService.buscarPorNombre(nombre);

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDtoResponse("No se encontraron empleados con el nombre: " + nombre, false));
        }
        return ResponseEntity.ok(empleados);
    }

    // ==========================================
    // QUERYS PERSONALIZADOS
    // ==========================================

    @GetMapping("/salario")
    public ResponseEntity<Object> buscarPorRangoSalario(
            @RequestParam BigDecimal salarioMin,
            @RequestParam BigDecimal salarioMax) {
        log.info("REST Request: Buscando empleados con salario entre {} y {}", salarioMin, salarioMax);
        List<EmpleadoDtoResponse> empleados = empleadoService.buscarPorRangoSalario(salarioMin, salarioMax);

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDtoResponse("No se encontraron empleados en ese rango de salario", false));
        }
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/fecha-contratacion")
    public ResponseEntity<Object> buscarPorFechaContratacion(
            @RequestParam LocalDate fecha) {
        log.info("REST Request: Buscando empleados contratados a partir de {}", fecha);
        List<EmpleadoDtoResponse> empleados = empleadoService.buscarPorFechaContratacion(fecha);

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDtoResponse("No se encontraron empleados contratados a partir de esa fecha", false));
        }
        return ResponseEntity.ok(empleados);
    }
}