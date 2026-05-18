package com.ecommers.inventario.service.Impl;

import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;

import com.ecommers.inventario.model.Inventory;
import com.ecommers.inventario.repository.InventoryRepository;
import com.ecommers.inventario.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    @Override
    public InventoryResponse addStock(InventoryRequest request) {
        Inventory inventory = repository.findByProductId(request.productId())
                .orElseGet(() -> {
                    Inventory newInv = new Inventory();
                    newInv.setProductId(request.productId());
                    newInv.setQuantity(0);
                    return newInv;
                });

        inventory.setQuantity(inventory.getQuantity() + request.quantity());
        Inventory saved = repository.save(inventory);
        return new InventoryResponse(saved.getId(), saved.getProductId(), saved.getQuantity());
    }

    @Override
    public InventoryResponse reduceStock(InventoryRequest request) {

        Inventory inventory = repository.findByProductId(request.productId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No hay registro de inventario para el producto " + request.productId()));

        if (inventory.getQuantity() < request.quantity()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        Inventory saved = repository.save(inventory);
        return new InventoryResponse(saved.getId(), saved.getProductId(), saved.getQuantity());
    }

    @Override
    public InventoryResponse checkStock(Long productId) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No hay inventario registrado para el producto " + productId));

        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }
}