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

    // QUERYS PERSONALIZADOS

    // Buscar pedidos por rango de total
    @Query("SELECT p FROM PedidoEntity p WHERE p.total BETWEEN :min AND :max AND p.activo = true")
    List<PedidoEntity> buscarPorRangoTotal(@Param("min") Double min, @Param("max") Double max);

    // Buscar pedidos activos de un cliente específico
    @Query("SELECT p FROM PedidoEntity p WHERE p.cliente.id = :clienteId AND p.activo = true")
    List<PedidoEntity> buscarPedidosActivosPorCliente(@Param("clienteId") Integer clienteId);

}
