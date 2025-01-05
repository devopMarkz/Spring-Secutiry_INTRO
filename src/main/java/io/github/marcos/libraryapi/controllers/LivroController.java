package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.livro.CreateLivroDTO;
import io.github.marcos.libraryapi.dto.livro.LivroResponseDTO;
import io.github.marcos.libraryapi.services.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/livros")
public class LivroController implements GenericController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> salvar(@Valid @RequestBody CreateLivroDTO createLivroDTO){
        LivroResponseDTO livroResponseDTO = livroService.insert(createLivroDTO);
        URI location = gerarHeaderLocation(livroResponseDTO.id());
        return ResponseEntity.created(location).body(livroResponseDTO);
    }

}
