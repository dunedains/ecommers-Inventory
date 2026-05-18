package com.ecommers.inventario.exception;

public record ErrorResponse(int status, String message, String timestamp) {
}