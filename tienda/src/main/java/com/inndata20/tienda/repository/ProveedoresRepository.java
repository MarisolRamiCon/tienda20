package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ProveedoresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProveedoresRepository extends JpaRepository<ProveedoresEntity,Integer> {
    List<ProveedoresEntity> findByActivoTrue();
}
