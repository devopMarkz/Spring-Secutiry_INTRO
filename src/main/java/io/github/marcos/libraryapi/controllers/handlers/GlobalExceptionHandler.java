package io.github.marcos.libraryapi.controllers.handlers;

import io.github.marcos.libraryapi.dto.ErroResposta;
import io.github.marcos.libraryapi.services.exceptions.AutorDuplicadoException;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AutorInexistenteException.class)
    public ResponseEntity<ErroResposta> autorInexistente(AutorInexistenteException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroResposta erroResposta = new ErroResposta(status.value(), e.getMessage(), List.of());
        return ResponseEntity.status(status.value()).body(erroResposta);
    }

    @ExceptionHandler(AutorDuplicadoException.class)
    public ResponseEntity<ErroResposta> autorDuplicado(AutorDuplicadoException e){
        ErroResposta erroResposta = ErroResposta.conflito(e.getMessage());
        return ResponseEntity.status(erroResposta.status()).body(erroResposta);
    }

}
