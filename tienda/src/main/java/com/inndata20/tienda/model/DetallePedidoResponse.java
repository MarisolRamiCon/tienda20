package com.inndata20.tienda.model;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.entity.ProductoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DetallePedidoResponse {
    private int id;
    private Optional<PedidoEntity> pedido;
    private Optional<ProductoEntity> producto;
    private int cantidad;
    private double precioUnitario;


}
