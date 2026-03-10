package com.inndata20.tienda.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "productos", url = "https://69ac8d709ca639a5217f29e2.mockapi.io")
public interface MockClient {

    @GetMapping("/productos")
    List<APIMock> listarProductos();

}