package com.ecommers.inventario.controller;

import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> addStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(service.addStock(request));
    }

    @PostMapping("/reduce")
    public ResponseEntity<InventoryResponse> reduceStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(service.reduceStock(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(service.checkStock(productId));
    }
}
