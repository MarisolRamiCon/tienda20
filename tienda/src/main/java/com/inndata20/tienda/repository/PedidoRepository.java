package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.PedidoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<PedidoEntity,Integer> {

    @Query("UPDATE PedidoEntity p SET p.activo = false WHERE p.id = :id")
    @Modifying
    @Transactional
    void eliminarPedido(@Param("id") Integer id);

    // JPA METODOS

    // Buscar por rango de fechas
    List<PedidoEntity> findByFechaPedidoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar por cliente
    List<PedidoEntity> findByCliente_Id(Integer clienteId);


}
