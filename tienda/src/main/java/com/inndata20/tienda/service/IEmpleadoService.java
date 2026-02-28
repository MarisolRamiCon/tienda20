package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.EmpleadoEntity;
import java.util.List;

public interface IEmpleadoService {

    List<EmpleadoEntity> listarEmpleados();
    EmpleadoEntity buscarPorId(Integer id);
    EmpleadoEntity guardarEmpleado(EmpleadoEntity empleado);
    EmpleadoEntity actualizarEmpleado(Integer id, EmpleadoEntity empleado);

}