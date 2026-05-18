package com.ecommers.inventario.service;

import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;

public interface InventoryService {
    InventoryResponse addStock(InventoryRequest request);
    InventoryResponse reduceStock(InventoryRequest request);
    InventoryResponse checkStock(Long productId);
}