package com.ecommers.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryDto {

    public record InventoryRequest(
            Long id,
            @NotNull Long productId,
            @NotNull @Min(1) Integer quantity) {
    }

    public record InventoryResponse(
            Long id,
            Long productId,
            Integer quantity) {
    }
}