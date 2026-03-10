package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.feign.APIMock;
import com.inndata20.tienda.feign.MockClient;
import com.inndata20.tienda.service.ImockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockService implements ImockService {

    private final MockClient mockClient;

    @Autowired
    public MockService(MockClient mockClient) {
        this.mockClient = mockClient;
    }

    @Override
    public List<APIMock> listarProductos() {
        return mockClient.listarProductos();
    }
}