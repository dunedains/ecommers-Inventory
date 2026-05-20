package com.ecommers.inventario.client;

import com.ecommers.inventario.dto.InventoryDto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${feign.client.product_url}")
public interface ProductClient {

    @GetMapping("/api/productos/{id}")
    ProductResponse getProductById(@PathVariable Long id);
}
