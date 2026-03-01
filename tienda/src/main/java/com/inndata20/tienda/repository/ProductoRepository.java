package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {



}
