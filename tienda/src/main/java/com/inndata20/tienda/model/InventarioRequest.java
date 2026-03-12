package com.inndata20.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InventarioRequest {
    private int producto;
    private int cantidadStock;
}
