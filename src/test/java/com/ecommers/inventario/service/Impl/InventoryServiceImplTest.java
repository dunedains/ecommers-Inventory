package com.ecommers.inventario.service.Impl;

import com.ecommers.inventario.client.ProductClient;
import com.ecommers.inventario.dto.InventoryDto.InventoryRequest;
import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.model.Inventory;
import com.ecommers.inventario.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del control de inventario.
 * Se mockean el repositorio y el cliente de productos.
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository repository;
    @Mock
    private ProductClient productClient;

    @InjectMocks
    private InventoryServiceImpl service;

    @Test
    @DisplayName("addStock: suma la cantidad al stock existente del producto")
    void addStock_existente_sumaCantidad() {
        // Given: el producto 10 ya tiene 5 unidades
        Inventory inv = new Inventory();
        inv.setId(1L);
        inv.setProductId(10L);
        inv.setQuantity(5);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(inv));
        when(repository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        // When: se agregan 3
        InventoryResponse response = service.addStock(new InventoryRequest(null, 10L, 3));

        // Then: total 8
        assertThat(response.quantity()).isEqualTo(8);
        verify(repository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("reduceStock: descuenta cuando hay stock suficiente")
    void reduceStock_suficiente_descuenta() {
        // Given: 10 unidades
        Inventory inv = new Inventory();
        inv.setProductId(10L);
        inv.setQuantity(10);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(inv));
        when(repository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        // When: se descuentan 3
        InventoryResponse response = service.reduceStock(new InventoryRequest(null, 10L, 3));

        // Then: quedan 7
        assertThat(response.quantity()).isEqualTo(7);
    }

    @Test
    @DisplayName("reduceStock: si no hay stock suficiente, lanza excepción y no descuenta")
    void reduceStock_insuficiente_lanzaExcepcion() {
        Inventory inv = new Inventory();
        inv.setProductId(10L);
        inv.setQuantity(2);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(inv));

        assertThatThrownBy(() -> service.reduceStock(new InventoryRequest(null, 10L, 5)))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("reduceStock: si el producto no tiene registro de inventario, lanza excepción")
    void reduceStock_sinRegistro_lanzaExcepcion() {
        when(repository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.reduceStock(new InventoryRequest(null, 99L, 1)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("checkStock: si el producto no tiene inventario, lanza excepción")
    void checkStock_sinRegistro_lanzaExcepcion() {
        when(repository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.checkStock(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
