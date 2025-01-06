package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.livro.CreateLivroDTO;
import io.github.marcos.libraryapi.dto.livro.LivroResponseDTO;
import io.github.marcos.libraryapi.services.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/livros")
public class LivroController implements GenericController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<Void> salvar(@Valid @RequestBody CreateLivroDTO createLivroDTO){
        LivroResponseDTO livroResponseDTO = livroService.insert(createLivroDTO);
        URI location = gerarHeaderLocation(livroResponseDTO.id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> obterDetalhes(@PathVariable String id){
        LivroResponseDTO livroResponseDTO = livroService.findById(id);
        return ResponseEntity.ok(livroResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable String id){
        livroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
