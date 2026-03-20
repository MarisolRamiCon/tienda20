package com.inndata20.tienda.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ForaneoEntity {
    private int id;
    private String name;
    private String company;
}
