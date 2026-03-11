package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.EmpleadoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
