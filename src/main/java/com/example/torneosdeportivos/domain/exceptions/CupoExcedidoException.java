package com.example.torneosdeportivos.domain.exceptions;

public class CupoExcedidoException extends RuntimeException {
    public CupoExcedidoException(String message) {
        super(message);
    }
}
