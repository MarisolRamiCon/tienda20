package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

public interface IEmpleadoService {

    List<EmpleadoEntity> listarEmpleados();
    EmpleadoEntity buscarPorId(Integer id);
    EmpleadoEntity guardarEmpleado(EmpleadoEntity empleado);
    EmpleadoEntity actualizarEmpleado(Integer id, EmpleadoEntity empleado);

}