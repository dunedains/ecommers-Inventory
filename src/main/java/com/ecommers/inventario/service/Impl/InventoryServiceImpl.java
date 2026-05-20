package com.ecommers.inventario.service.Impl;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final ProductClient productClient;

    @Override
    @Transactional
    public InventoryResponse addStock(InventoryRequest request) {
        log.info("Agregando stock productId={} qty={}", request.productId(), request.quantity());
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
        log.info("Stock actualizado productId={} total={}", saved.getProductId(), saved.getQuantity());
        return new InventoryResponse(saved.getId(), saved.getProductId(), saved.getQuantity());
    }

    @Override
    @Transactional
    public InventoryResponse reduceStock(InventoryRequest request) {
        log.info("Reduciendo stock productId={} qty={}", request.productId(), request.quantity());
        Inventory inventory = repository.findByProductId(request.productId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No inventory record for product " + request.productId()));

        if (inventory.getQuantity() < request.quantity()) {
            log.warn("Stock insuficiente productId={} disponible={} solicitado={}", request.productId(), inventory.getQuantity(), request.quantity());
            throw new IllegalArgumentException("Insufficient stock. Available: " + inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        Inventory saved = repository.save(inventory);
        log.info("Stock reducido productId={} restante={}", saved.getProductId(), saved.getQuantity());
        return new InventoryResponse(saved.getId(), saved.getProductId(), saved.getQuantity());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse checkStock(Long productId) {
        log.info("Consultando stock productId={}", productId);
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No inventory registered for product " + productId));
        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }
}
