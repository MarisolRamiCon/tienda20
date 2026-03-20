package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface InventarioRepository extends JpaRepository<InventarioEntity,Integer> {
    public List<InventarioEntity> findByActivoTrue();
}
