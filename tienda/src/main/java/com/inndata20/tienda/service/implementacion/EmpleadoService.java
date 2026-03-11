package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.repository.EmpleadoRepository;
import com.inndata20.tienda.service.IEmpleadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<EmpleadoEntity> listarEmpleados() {
        return empleadoRepository.findAll()
                .stream()
                .filter(EmpleadoEntity::getActivo) // ✅ solo activos
                .toList();
    }

    @Override
    public EmpleadoEntity buscarPorId(Integer id) {
        return empleadoRepository.findById(id)
                .filter(EmpleadoEntity::getActivo) // ✅ solo si está activo
                .orElse(null);
    }

    public class EmpleadoServiceException extends RuntimeException {
        public EmpleadoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    @Transactional
    @Override
    public EmpleadoEntity guardarEmpleado(EmpleadoEntity empleado) {
        try {
            return empleadoRepository.save(empleado);
        } catch (DataAccessException e) {
            throw new EmpleadoServiceException("Error de acceso a la base de datos", e);
        } catch (Exception e) {
            throw new EmpleadoServiceException("Error inesperado al guardar empleado", e);
        }
    }


    @Transactional
    @Override
    public EmpleadoEntity actualizarEmpleado(Integer id, EmpleadoEntity empleado) {

        try {

            EmpleadoEntity empleadoExistente = empleadoRepository.findById(id).orElse(null);
            if (empleadoExistente != null) {
                empleadoExistente.setNombre(empleado.getNombre());
                empleadoExistente.setApellido(empleado.getApellido());
                empleadoExistente.setPuesto(empleado.getPuesto());
                empleadoExistente.setSalario(empleado.getSalario());
                empleadoExistente.setFechaContratacion(empleado.getFechaContratacion());
                return empleadoRepository.save(empleadoExistente);
            }
            return null;

        } catch (DataAccessException e) {
            logger.error("Error de acceso a la base de datos al actualizar empleado con id {}: {}", id, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar empleado con id {}: {}", id, e.getMessage());
            return null;
        }
    }


    private static final Logger logger = LoggerFactory.getLogger(EmpleadoService.class);
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
            logger.error("Error de acceso a la base de datos al eliminar empleado con id {}: {}", id, e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar empleado con id {}: {}", id, e.getMessage());
            return false;
        }
    }
}





