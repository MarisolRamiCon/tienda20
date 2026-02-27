package com.inndata20.tienda.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;
    @Column(name = "cliente_id")
    private Integer clienteId;
    @Column(name = "total")
    private Double total;

    public PedidoEntity() {
    }

    public PedidoEntity(Integer id, LocalDate fechaPedido, Integer clienteId, Double total) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.clienteId = clienteId;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
