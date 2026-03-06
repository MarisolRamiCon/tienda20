package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ProductoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    @Query("UPDATE ProductoEntity p SET p.activo = false WHERE p.id = :id")
    @Modifying
    @Transactional
    void eliminarProducto(@Param("id") Integer id);



}
