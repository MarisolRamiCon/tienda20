package com.inndata20.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoDtoResponse {

    private LocalDate fechaPedido;
    private Double total;
    private Integer clienteId;
}