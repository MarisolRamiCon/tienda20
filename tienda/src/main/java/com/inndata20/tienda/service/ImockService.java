package com.inndata20.tienda.service;

import com.inndata20.tienda.feign.APIMock;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ImockService {


    List<APIMock> listarProductos();


}
