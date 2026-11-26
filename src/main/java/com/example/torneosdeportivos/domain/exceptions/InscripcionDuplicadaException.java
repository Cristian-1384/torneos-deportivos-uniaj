package com.example.torneosdeportivos.domain.exceptions;

public class InscripcionDuplicadaException extends RuntimeException {
    public InscripcionDuplicadaException(String message) {
        super(message);
    }
}
