package com.ecommers.inventario.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
}
