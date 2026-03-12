package com.inndata20.tienda.model;

import com.inndata20.tienda.entity.ProductoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InventarioResponse {
    private int id;
    private ProductoEntity producto;
    private int cantidadStock;
}
