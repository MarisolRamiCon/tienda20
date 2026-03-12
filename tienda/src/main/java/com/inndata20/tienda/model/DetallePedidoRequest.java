package com.inndata20.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DetallePedidoRequest {
    private int id;
    private int pedido;
    private int producto;
    private int cantidad;
    private double precioUnitario;
}
