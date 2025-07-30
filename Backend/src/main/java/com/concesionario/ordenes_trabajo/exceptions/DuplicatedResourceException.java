package com.concesionario.ordenes_trabajo.exceptions;

public class DuplicatedResourceException extends RuntimeException {
    public DuplicatedResourceException(String message) {
        super(message);
    }
}
