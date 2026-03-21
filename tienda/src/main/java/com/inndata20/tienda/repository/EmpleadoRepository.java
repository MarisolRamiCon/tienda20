package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.EmpleadoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity,Integer> {

    @Query("UPDATE EmpleadoEntity p SET p.activo = false WHERE p.id = :id")
    @Modifying
    @Transactional
    void eliminarEmpleado(@Param("id") Integer id);


    // JPA METODOS PERSONALIZADOS

    // Buscar empleados por su puesto (ej. "Cajero", "Gerente")
    List<EmpleadoEntity> findByPuesto(String puesto);

    // Buscar empleados por nombre (coincidencia parcial sin importar mayúsculas/minúsculas)
    List<EmpleadoEntity> findByNombreContainingIgnoreCase(String nombre);


    // QUERYS

    // Buscar empleados por rango de salario
    @Query("SELECT e FROM EmpleadoEntity e WHERE e.salario BETWEEN :min AND :max AND e.activo = true")
    List<EmpleadoEntity> buscarPorRangoSalario(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    // Buscar empleados contratados a partir de una fecha
    @Query("SELECT e FROM EmpleadoEntity e WHERE e.fechaContratacion >= :fecha AND e.activo = true")
    List<EmpleadoEntity> buscarPorFechaContratacion(@Param("fecha") LocalDate fecha);
}
