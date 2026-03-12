package com.inndata20.tienda.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "foraneo", url = "https://69a8deea32e2d46caf449db4.mockapi.io/api/v1/")

public interface ForaneoClient {
    @GetMapping("/foraneo")
    public List<ForaneoEntity> readAll();
}
