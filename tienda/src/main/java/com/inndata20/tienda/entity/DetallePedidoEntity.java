package com.inndata20.tienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalles_de_pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "pedido")
    private PedidoEntity pedido;
    @ManyToOne
    @JoinColumn(name = "producto")
    private ProductoEntity producto;
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "precio_unitario")
    private double precioUnitario;
    @Column(name = "activo",nullable = false)
    private boolean activo = true;
}
