package io.github.marcos.libraryapi.services.exceptions;

public class ClientInexistenteException extends RuntimeException {
    public ClientInexistenteException(String message) {
        super(message);
    }
}
