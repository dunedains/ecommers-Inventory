package com.ecommers.inventario.client;

import com.ecommers.inventario.dto.InventoryDto.ProductoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service", url = "${feign.client.producto-url}")
public interface ProductoClient {

    @GetMapping("/api/productos/{id}")
    ProductoResponse getProductoById(@PathVariable Long id);
}