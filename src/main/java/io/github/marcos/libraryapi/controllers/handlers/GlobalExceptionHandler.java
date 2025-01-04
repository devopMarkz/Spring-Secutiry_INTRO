package io.github.marcos.libraryapi.controllers.handlers;

import io.github.marcos.libraryapi.dto.erro.ErroCampo;
import io.github.marcos.libraryapi.dto.erro.ErroResposta;
import io.github.marcos.libraryapi.services.exceptions.AutorDuplicadoException;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import io.github.marcos.libraryapi.services.exceptions.OperacaoNaoPermitidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
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

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<ErroResposta> operacaoNaoPermitida(OperacaoNaoPermitidaException e){
        ErroResposta erroResposta = ErroResposta.respostaPadrao(e.getMessage());
        return ResponseEntity.status(erroResposta.status()).body(erroResposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> methodArgumentNotValid(MethodArgumentNotValidException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResposta erro = new ErroResposta(status.value(), "Erro de validação de dados.", new ArrayList<>());
        for (FieldError f : e.getFieldErrors()){
            erro.erros().add(new ErroCampo(f.getField(), f.getDefaultMessage()));
        }
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> httpMessageNotReadable(HttpMessageNotReadableException e){
        ErroResposta erro = ErroResposta.respostaPadrao("Corpo da requisição inválido.");
        return ResponseEntity.status(erro.status()).body(erro);
    }

}
