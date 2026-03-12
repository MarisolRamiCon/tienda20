package com.inndata20.tienda.service;

import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
import com.inndata20.tienda.model.MensajeDtoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IEmpleadoService {

    List<EmpleadoDtoResponse> listarEmpleados();
     EmpleadoDtoResponse buscarPorId(EmpleadoDtoRequest empleadoDtoRequest);

    MensajeDtoResponse guardarEmpleado(EmpleadoDtoRequest dto);
    MensajeDtoResponse actualizarEmpleado(Integer id, EmpleadoDtoRequest dto);
    boolean eliminarEmpleado(Integer id);

    // --- MÉTODOS JPA PERSONALIZADOS ---
    List<EmpleadoDtoResponse> buscarPorPuesto(String puesto);
    List<EmpleadoDtoResponse> buscarPorNombre(String nombre);

    // QUERYS PERSONALIZADOS

    List<EmpleadoDtoResponse> buscarPorRangoSalario(BigDecimal min, BigDecimal max);
    List<EmpleadoDtoResponse> buscarPorFechaContratacion(LocalDate fecha);
}