package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ProductoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    @Query("UPDATE ProductoEntity p SET p.activo = false WHERE p.id = :id")



    @Modifying
    @Transactional
    void eliminarProducto(@Param("id") Integer id);

    // JPA PERSONALIZADOS

        // Voy a seleccionar los productos que pertenezcan a la misma categoria, pero que el precio sea menor al que voy a colorcar

        public List<ProductoEntity>findByCategoriaAndPrecioLessThan(String categoria, Double precio);

        // Seleccionar productos cuyo stock esté entre X y Y

        public List<ProductoEntity> findByStockBetween(Integer stockMin, Integer stockMax);


    // QUERYS

    @Query("SELECT p FROM ProductoEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.categoria = :categoria AND p.activo = true")
    List<ProductoEntity> buscarPorNombreYCategoria(@Param("nombre") String nombre, @Param("categoria") String categoria);

    // Buscar productos activos por proveedor
    @Query("SELECT p FROM ProductoEntity p WHERE p.proveedor.id = :proveedorId AND p.activo = true")
    List<ProductoEntity> buscarPorProveedor(@Param("proveedorId") Integer proveedorId);
}
