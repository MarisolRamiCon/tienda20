package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PedidoRepository extends JpaRepository<PedidoEntity,Integer> {
}
