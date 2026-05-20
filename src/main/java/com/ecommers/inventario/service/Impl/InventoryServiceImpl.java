package com.ecommers.inventario.service.Impl;

import com.ecommers.inventario.client.ProductClient;
import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.model.Inventory;
import com.ecommers.inventario.repository.InventoryRepository;
import com.ecommers.inventario.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final ProductClient productClient;

    @Override
    @Transactional
    public InventoryResponse addStock(InventoryRequest request) {
        productClient.getProductById(request.productId());

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
    @Transactional
    public InventoryResponse reduceStock(InventoryRequest request) {
        Inventory inventory = repository.findByProductId(request.productId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No inventory record for product " + request.productId()));

        if (inventory.getQuantity() < request.quantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        Inventory saved = repository.save(inventory);
        return new InventoryResponse(saved.getId(), saved.getProductId(), saved.getQuantity());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse checkStock(Long productId) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No inventory registered for product " + productId));

        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }
}
