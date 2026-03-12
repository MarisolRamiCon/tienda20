package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.repository.EmpleadoRepository;
import com.inndata20.tienda.service.IEmpleadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EmpleadoService implements IEmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public List<EmpleadoDtoResponse> listarEmpleados() {
        log.info("Service: Consultando todos los empleados activos en la base de datos");
        return empleadoRepository.findAll()
                .stream()
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    @Override
    public EmpleadoDtoResponse buscarPorId(EmpleadoDtoRequest empleadoDtoRequest) {
        log.info("Service: Buscando empleado por ID: {}", empleadoDtoRequest.getId());
        EmpleadoDtoResponse response = empleadoRepository.findById(empleadoDtoRequest.getId())
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .orElse(null);

        if (response == null) {
            log.warn("Service: Empleado con ID {} no encontrado o está inactivo", empleadoDtoRequest.getId());
        } else {
            log.info("Service: Empleado '{}' encontrado con éxito", response.getNombre());
        }
        return response;
    }

    @Transactional
    @Override
    public MensajeDtoResponse guardarEmpleado(EmpleadoDtoRequest dto) {
        log.info("Service: Iniciando proceso para guardar nuevo empleado: {}", dto.getNombre());
        try {
            EmpleadoEntity empleado = new EmpleadoEntity();
            empleado.setNombre(dto.getNombre());
            empleado.setApellido(dto.getApellido());
            empleado.setPuesto(dto.getPuesto());
            empleado.setSalario(dto.getSalario());
            empleado.setFechaContratacion(dto.getFechaContratacion());
            empleado.setActivo(true);

            empleadoRepository.save(empleado);
            log.info("Service: Empleado '{}' guardado exitosamente en la BD", dto.getNombre());
            return new MensajeDtoResponse("Empleado guardado exitosamente",true);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar guardar el empleado {}", dto.getNombre(), e);
            return  new MensajeDtoResponse("Error de base de datos al guardar empleado",false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse actualizarEmpleado(Integer id, EmpleadoDtoRequest dto) {
        log.info("Service: Iniciando proceso de actualización para el empleado ID: {}", id);
        try {
            EmpleadoEntity empleadoExistente = empleadoRepository.findById(id).orElse(null);
            if (empleadoExistente == null || !empleadoExistente.getActivo()) {
                log.warn("Service: No se puede actualizar. Empleado con ID {} no encontrado o inactivo", id);
                return new MensajeDtoResponse("Empleado no encontrado o inactivo", false);
            }

            empleadoExistente.setNombre(dto.getNombre());
            empleadoExistente.setApellido(dto.getApellido());
            empleadoExistente.setPuesto(dto.getPuesto());
            empleadoExistente.setSalario(dto.getSalario());
            empleadoExistente.setFechaContratacion(dto.getFechaContratacion());

            empleadoRepository.save(empleadoExistente);
            log.info("Service: Empleado con ID {} actualizado exitosamente", id);
            return new MensajeDtoResponse("Empleado actualizado exitosamente", true);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar actualizar el empleado ID {}", id, e);
            return new MensajeDtoResponse("Error de base de datos al actualizar empleado", false);
        }
    }

    @Transactional
    @Override
    public boolean eliminarEmpleado(Integer id) {
        log.info("Service: Solicitud para eliminar lógicamente el empleado con ID: {}", id);
        try {
            if (empleadoRepository.existsById(id)) {
                empleadoRepository.eliminarEmpleado(id);
                log.info("Service: Empleado con ID {} eliminado lógicamente de la BD", id);
                return true;
            }
            log.warn("Service: No se pudo eliminar. Empleado con ID {} no existe", id);
            return false;
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al eliminar el empleado ID {}", id, e);
            throw new RuntimeException("Error de acceso a la base de datos", e);
        }
    }

    // JPA PERSONALIZADOS

    @Override
    public List<EmpleadoDtoResponse> buscarPorPuesto(String puesto) {
        log.info("Service: Consultando BD por puesto '{}'", puesto);
        return empleadoRepository.findByPuesto(puesto)
                .stream()
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    @Override
    public List<EmpleadoDtoResponse> buscarPorNombre(String nombre) {
        log.info("Service: Consultando BD por nombre que contenga '{}'", nombre);
        return empleadoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .filter(EmpleadoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    // QUERYS PERSONALIZADOS

    @Override
    public List<EmpleadoDtoResponse> buscarPorRangoSalario(BigDecimal min, BigDecimal max) {
        log.info("Service: Consultando BD por salario entre {} y {}", min, max);
        return empleadoRepository.buscarPorRangoSalario(min, max)
                .stream()
                .map(this::mapearADto)
                .toList();
    }

    @Override
    public List<EmpleadoDtoResponse> buscarPorFechaContratacion(LocalDate fecha) {
        log.info("Service: Consultando BD por fecha de contratación a partir de {}", fecha);
        return empleadoRepository.buscarPorFechaContratacion(fecha)
                .stream()
                .map(this::mapearADto)
                .toList();
    }

    // MÉTODO AUXILIAR
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