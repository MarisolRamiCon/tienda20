package com.inndata20.tienda.service;

import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;

import java.util.List;

public interface IEmpleadoService {

    List<EmpleadoDtoResponse> listarEmpleados();
    EmpleadoDtoResponse buscarPorId(Integer id);
    String guardarEmpleado(EmpleadoDtoRequest dto);
    String actualizarEmpleado(Integer id, EmpleadoDtoRequest dto);
    boolean eliminarEmpleado(Integer id);

    // --- MÉTODOS JPA PERSONALIZADOS ---
    List<EmpleadoDtoResponse> buscarPorPuesto(String puesto);
    List<EmpleadoDtoResponse> buscarPorNombre(String nombre);

}