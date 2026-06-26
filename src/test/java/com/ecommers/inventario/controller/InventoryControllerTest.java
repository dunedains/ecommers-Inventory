package com.ecommers.inventario.controller;

import com.ecommers.inventario.dto.InventoryDto.InventoryResponse;
import com.ecommers.inventario.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService service;

    @Test
    @DisplayName("POST /api/inventory/add -> 200")
    void addStock_devuelve200() throws Exception {
        when(service.addStock(any())).thenReturn(new InventoryResponse(1L, 10L, 8));

        mockMvc.perform(post("/api/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10,\"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(8));
    }

    @Test
    @DisplayName("POST /api/inventory/reduce -> 200")
    void reduceStock_devuelve200() throws Exception {
        when(service.reduceStock(any())).thenReturn(new InventoryResponse(1L, 10L, 2));

        mockMvc.perform(post("/api/inventory/reduce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10,\"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    @DisplayName("GET /api/inventory/{productId} -> 200")
    void getStock_devuelve200() throws Exception {
        when(service.checkStock(10L)).thenReturn(new InventoryResponse(1L, 10L, 5));

        mockMvc.perform(get("/api/inventory/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(10));
    }

    @Test
    @DisplayName("POST /api/inventory/add con cantidad inválida -> 400")
    void addStock_invalido_devuelve400() throws Exception {
        mockMvc.perform(post("/api/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10,\"quantity\":0}"))
                .andExpect(status().isBadRequest());
    }
}
