package com.inndata20.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoDtoResponse {

    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;
    private Integer proveedor;
    private Integer stock;
}