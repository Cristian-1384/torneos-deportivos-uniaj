package com.example.torneosdeportivos.domain.exceptions;

public class TorneoNoDisponibleException extends RuntimeException {
    public TorneoNoDisponibleException(String message) {
        super(message);
    }
}
