package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
import com.inndata20.tienda.repository.EmpleadoRepository;
import com.inndata20.tienda.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService implements IEmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public List<EmpleadoDtoResponse> listarEmpleados() {
        return empleadoRepository.findAll()
                .stream()
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto) // Usamos el método auxiliar
                .toList();
    }

    @Override
    public EmpleadoDtoResponse buscarPorId(Integer id) {
        return empleadoRepository.findById(id)
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .orElse(null);
    }

    @Transactional
    @Override
    public String guardarEmpleado(EmpleadoDtoRequest dto) {
        try {
            EmpleadoEntity empleado = new EmpleadoEntity();
            empleado.setNombre(dto.getNombre());
            empleado.setApellido(dto.getApellido());
            empleado.setPuesto(dto.getPuesto());
            empleado.setSalario(dto.getSalario());
            empleado.setFechaContratacion(dto.getFechaContratacion());
            empleado.setActivo(true);

            empleadoRepository.save(empleado);
            return "Empleado creado exitosamente";
        } catch (DataAccessException e) {
            throw new RuntimeException("Error de base de datos al guardar empleado", e);
        }
    }

    @Transactional
    @Override
    public String actualizarEmpleado(Integer id, EmpleadoDtoRequest dto) {
        try {
            EmpleadoEntity empleadoExistente = empleadoRepository.findById(id).orElse(null);
            if (empleadoExistente == null || !empleadoExistente.getActivo()) {
                return "Empleado no encontrado o inactivo";
            }

            empleadoExistente.setNombre(dto.getNombre());
            empleadoExistente.setApellido(dto.getApellido());
            empleadoExistente.setPuesto(dto.getPuesto());
            empleadoExistente.setSalario(dto.getSalario());
            empleadoExistente.setFechaContratacion(dto.getFechaContratacion());

            empleadoRepository.save(empleadoExistente);
            return "Empleado actualizado exitosamente";
        } catch (DataAccessException e) {
            throw new RuntimeException("Error de base de datos al actualizar empleado", e);
        }
    }

    @Transactional
    @Override
    public boolean eliminarEmpleado(Integer id) {
        try {
            if (empleadoRepository.existsById(id)) {
                empleadoRepository.eliminarEmpleado(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error de acceso a la base de datos", e);
        }
    }

    // --- TUS NUEVOS MÉTODOS JPA PERSONALIZADOS ---

    @Override
    public List<EmpleadoDtoResponse> buscarPorPuesto(String puesto) {
        return empleadoRepository.findByPuesto(puesto)
                .stream()
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    @Override
    public List<EmpleadoDtoResponse> buscarPorNombre(String nombre) {
        return empleadoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    // --- MÉTODO AUXILIAR (HELPER) PARA NO REPETIR CÓDIGO ---
    private EmpleadoDtoResponse mapearADto(EmpleadoEntity empleado) {
        EmpleadoDtoResponse dto = new EmpleadoDtoResponse();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setPuesto(empleado.getPuesto());
        dto.setSalario(empleado.getSalario());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        return dto;
    }
}