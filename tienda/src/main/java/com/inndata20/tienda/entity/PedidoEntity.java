package com.inndata20.tienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")

// LOOMBOK

@AllArgsConstructor
@NoArgsConstructor
@Data


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
    @Column(name = "activo")
    private Boolean activo = true;

}
