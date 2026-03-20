package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity,Integer> {
    public List<DetallePedidoEntity> findByActivoTrue();
}
