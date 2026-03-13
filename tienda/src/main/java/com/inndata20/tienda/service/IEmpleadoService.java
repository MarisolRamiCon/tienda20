package com.inndata20.tienda.service;

import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
import com.inndata20.tienda.model.MensajeDtoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IEmpleadoService {

    List<EmpleadoDtoResponse> listarEmpleados();

    // 👇 AQUÍ ESTÁ EL CAMBIO QUE QUITA EL ERROR 👇
    EmpleadoDtoResponse buscarPorId(EmpleadoDtoRequest empleadoRequest);

    MensajeDtoResponse guardarEmpleado(EmpleadoDtoRequest empleadoRequest);

    MensajeDtoResponse actualizarEmpleado(Integer id, EmpleadoDtoRequest empleadoRequest);

    MensajeDtoResponse eliminarEmpleado(Integer id);

    // --- MÉTODOS JPA PERSONALIZADOS ---
    List<EmpleadoDtoResponse> buscarPorPuesto(String puesto);

    List<EmpleadoDtoResponse> buscarPorNombre(String nombre);

    // --- QUERYS PERSONALIZADOS ---
    List<EmpleadoDtoResponse> buscarPorRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax);

    List<EmpleadoDtoResponse> buscarPorFechaContratacion(LocalDate fecha);
}