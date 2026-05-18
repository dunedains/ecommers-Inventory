package com.ecommers.inventario.service.Impl;

import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.model.Inventory;
import com.ecommers.inventario.repository.InventoryRepository;
import com.ecommers.inventario.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    @Override
    public InventoryResponse addStock(InventoryRequest request) {
        log.info("Agregando {} unidades al producto {}", request.quantity(), request.productId());

        Inventory inventory = repository.findByProductId(request.productId())
                .orElseGet(() -> {
                    Inventory newInv = new Inventory();
                    newInv.setProductId(request.productId());
                    newInv.setQuantity(0);
                    return newInv;
                });

        inventory.setQuantity(inventory.getQuantity() + request.quantity());
        repository.save(inventory);

        log.info("Stock actualizado. Nuevo total: {}", inventory.getQuantity());
        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }

    @Override
    public InventoryResponse reduceStock(InventoryRequest request) {
        log.info("Intentando reducir {} unidades del producto {}", request.quantity(), request.productId());

        Inventory inventory = repository.findByProductId(request.productId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No hay registro de inventario para el producto " + request.productId()));

        if (inventory.getQuantity() < request.quantity()) {
            log.error("Stock insuficiente para el producto {}. Disponible: {}, Solicitado: {}",
                    request.productId(), inventory.getQuantity(), request.quantity());
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        repository.save(inventory);

        log.info("Stock reducido exitosamente. Restante: {}", inventory.getQuantity());
        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }

    @Override
    public InventoryResponse checkStock(Long productId) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No hay inventario registrado para el producto " + productId));

        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }
}