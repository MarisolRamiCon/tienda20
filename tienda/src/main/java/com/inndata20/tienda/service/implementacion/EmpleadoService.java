package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.repository.EmpleadoRepository;
import com.inndata20.tienda.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService implements IEmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Override
    public List<EmpleadoEntity> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public EmpleadoEntity buscarPorId(Integer id){
       return empleadoRepository.findById(id).orElse(null);
    }

    @Override
    public EmpleadoEntity guardarPersona(PersonaEntity empleado){

        return empleadoRepository.save(empleado);

    }

    @Override
    public EmpleadoEntity actualizarEmpleado(Integer id, EmpleadoEntity empleado) {
        EmpleadoEntity empleadoExistente = empleadoRepository.findById(id).orElse(null);
        if (empleadoExistente != null) {
            empleadoExistente.setNombre(empleado.getNombre());
            empleadoExistente.setDireccion(empleado.getDireccion());
            empleadoExistente.setEdad(empleado.getEdad());
            empleadoExistente.setId_departamento(empleado.getId_departamento());
            return personaRepository.save(empleadoExistente);
        }
        return null;
    }



}


