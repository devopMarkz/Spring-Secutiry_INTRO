package io.github.marcos.libraryapi.services.exceptions;

public class LivroInexistenteException extends RuntimeException {
    public LivroInexistenteException(String message) {
        super(message);
    }
}
