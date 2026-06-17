package com.ecommers.inventario.controller;

import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/add")
    public ResponseEntity<EntityModel<InventoryResponse>> addStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(toModel(service.addStock(request)));
    }

    @PostMapping("/reduce")
    public ResponseEntity<EntityModel<InventoryResponse>> reduceStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(toModel(service.reduceStock(request)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<EntityModel<InventoryResponse>> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(toModel(service.checkStock(productId)));
    }

    private EntityModel<InventoryResponse> toModel(InventoryResponse inventory) {
        return EntityModel.of(inventory,
                linkTo(methodOn(InventoryController.class).getStock(inventory.productId())).withSelfRel());
    }
}
