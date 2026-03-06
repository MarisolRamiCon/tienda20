package com.inndata20.tienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "productos")

// LOOMBOK

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "proveedor")
    private ProveedoresEntity proveedor;



}
