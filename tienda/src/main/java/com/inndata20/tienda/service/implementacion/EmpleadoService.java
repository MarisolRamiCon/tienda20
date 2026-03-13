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
        try {
            return empleadoRepository.findAll()
                    .stream()
                    .filter(EmpleadoEntity::getActivo)
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al consultar empleados", e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al consultar empleados", e);
            return List.of();
        }
    }

    @Override
    public EmpleadoDtoResponse buscarPorId(EmpleadoDtoRequest empleadoDtoRequest) {
        log.info("Service: Buscando empleado por ID: {}", empleadoDtoRequest.getId());
        try {
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
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar empleado con ID {}", empleadoDtoRequest.getId(), e);
            return null;
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar empleado con ID {}", empleadoDtoRequest.getId(), e);
            return null;
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse guardarEmpleado(EmpleadoDtoRequest empleadoRequest) {
        log.info("Service: Iniciando proceso para guardar nuevo empleado: {}", empleadoRequest.getNombre());
        try {
            EmpleadoEntity empleado = new EmpleadoEntity();
            empleado.setNombre(empleadoRequest.getNombre());
            empleado.setApellido(empleadoRequest.getApellido());
            empleado.setPuesto(empleadoRequest.getPuesto());
            empleado.setSalario(empleadoRequest.getSalario());
            empleado.setFechaContratacion(empleadoRequest.getFechaContratacion());
            empleado.setActivo(true);

            empleadoRepository.save(empleado);
            log.info("Service: Empleado '{}' guardado exitosamente en la BD", empleadoRequest.getNombre());
            return new MensajeDtoResponse("Empleado guardado exitosamente", true);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar guardar el empleado {}", empleadoRequest.getNombre(), e);
            return new MensajeDtoResponse("Error de base de datos al guardar empleado", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al guardar el empleado {}", empleadoRequest.getNombre(), e);
            return new MensajeDtoResponse("Error inesperado al guardar empleado", false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse actualizarEmpleado(Integer id, EmpleadoDtoRequest empleadoRequest) {
        log.info("Service: Iniciando proceso de actualización para el empleado ID: {}", id);
        try {
            EmpleadoEntity empleadoExistente = empleadoRepository.findById(id).orElse(null);
            if (empleadoExistente == null || !empleadoExistente.getActivo()) {
                log.warn("Service: No se puede actualizar. Empleado con ID {} no encontrado o inactivo", id);
                return new MensajeDtoResponse("Empleado no encontrado o inactivo", false);
            }

            empleadoExistente.setNombre(empleadoRequest.getNombre());
            empleadoExistente.setApellido(empleadoRequest.getApellido());
            empleadoExistente.setPuesto(empleadoRequest.getPuesto());
            empleadoExistente.setSalario(empleadoRequest.getSalario());
            empleadoExistente.setFechaContratacion(empleadoRequest.getFechaContratacion());

            empleadoRepository.save(empleadoExistente);
            log.info("Service: Empleado con ID {} actualizado exitosamente", id);
            return new MensajeDtoResponse("Empleado actualizado exitosamente", true);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar actualizar el empleado ID {}", id, e);
            return new MensajeDtoResponse("Error de base de datos al actualizar empleado", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al actualizar el empleado ID {}", id, e);
            return new MensajeDtoResponse("Error inesperado al actualizar empleado", false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse eliminarEmpleado(Integer id) {
        log.info("Service: Solicitud para eliminar lógicamente el empleado con ID: {}", id);
        try {
            if (empleadoRepository.existsById(id)) {
                empleadoRepository.eliminarEmpleado(id);
                log.info("Service: Empleado con ID {} eliminado lógicamente de la BD", id);
                return new MensajeDtoResponse("Empleado eliminado exitosamente", true);
            }
            log.warn("Service: No se pudo eliminar. Empleado con ID {} no existe", id);
            return new MensajeDtoResponse("Empleado no encontrado", false);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al eliminar el empleado ID {}", id, e);
            return new MensajeDtoResponse("Error de base de datos al eliminar empleado", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al eliminar el empleado ID {}", id, e);
            return new MensajeDtoResponse("Error inesperado al eliminar empleado", false);
        }
    }

    // JPA PERSONALIZADOS

    @Override
    public List<EmpleadoDtoResponse> buscarPorPuesto(String puesto) {
        log.info("Service: Consultando BD por puesto '{}'", puesto);
        try {
            return empleadoRepository.findByPuesto(puesto)
                    .stream()
                    .filter(EmpleadoEntity::getActivo)
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por puesto '{}'", puesto, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por puesto '{}'", puesto, e);
            return List.of();
        }
    }

    @Override
    public List<EmpleadoDtoResponse> buscarPorNombre(String nombre) {
        log.info("Service: Consultando BD por nombre que contenga '{}'", nombre);
        try {
            return empleadoRepository.findByNombreContainingIgnoreCase(nombre)
                    .stream()
                    .filter(EmpleadoEntity::getActivo)
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por nombre '{}'", nombre, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por nombre '{}'", nombre, e);
            return List.of();
        }
    }

    // QUERYS PERSONALIZADOS

    @Override
    public List<EmpleadoDtoResponse> buscarPorRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax) {
        log.info("Service: Consultando BD por salario entre {} y {}", salarioMin, salarioMax);
        try {
            return empleadoRepository.buscarPorRangoSalario(salarioMin, salarioMax)
                    .stream()
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por rango de salario", e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por rango de salario", e);
            return List.of();
        }
    }

    @Override
    public List<EmpleadoDtoResponse> buscarPorFechaContratacion(LocalDate fecha) {
        log.info("Service: Consultando BD por fecha de contratación a partir de {}", fecha);
        try {
            return empleadoRepository.buscarPorFechaContratacion(fecha)
                    .stream()
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por fecha de contratación '{}'", fecha, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por fecha de contratación '{}'", fecha, e);
            return List.of();
        }
    }


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