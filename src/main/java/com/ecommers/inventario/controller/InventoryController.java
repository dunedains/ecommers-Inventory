package com.ecommers.inventario.controller;

import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Control de stock por producto; valida la existencia del producto vía Feign")
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/add")
    @Operation(summary = "Agregar stock a un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "400", description = "Cantidad inválida"),
            @ApiResponse(responseCode = "404", description = "El producto no existe en el catálogo")
    })
    public ResponseEntity<InventoryResponse> addStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(service.addStock(request));
    }

    @PostMapping("/reduce")
    @Operation(summary = "Descontar stock de un producto",
            description = "Falla si el stock disponible es menor a la cantidad solicitada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock descontado"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o cantidad inválida"),
            @ApiResponse(responseCode = "404", description = "El producto no tiene registro de inventario")
    })
    public ResponseEntity<InventoryResponse> reduceStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(service.reduceStock(request));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Consultar el stock de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actual del producto"),
            @ApiResponse(responseCode = "404", description = "El producto no tiene registro de inventario")
    })
    public ResponseEntity<InventoryResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(service.checkStock(productId));
    }
}
