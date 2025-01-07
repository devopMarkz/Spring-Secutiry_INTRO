package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.livro.CreateLivroDTO;
import io.github.marcos.libraryapi.dto.livro.LivroResponseDTO;
import io.github.marcos.libraryapi.model.GeneroLivro;
import io.github.marcos.libraryapi.services.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> pesquisarPorFiltros(@RequestParam(name = "isbn", required = false) String isbn,
                                                                      @RequestParam(name = "titulo", required = false) String titulo,
                                                                      @RequestParam(name = "nome-autor", required = false) String nomeAutor,
                                                                      @RequestParam(name = "genero", required = false) GeneroLivro genero,
                                                                      @RequestParam(name = "ano-publicacao", required = false) Integer anoPublicacao) {
        List<LivroResponseDTO> livroResponseDTOS = livroService.findByFilters(isbn, titulo, nomeAutor, genero, anoPublicacao);
        return ResponseEntity.ok(livroResponseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarPorId(@PathVariable String id, @RequestBody @Valid CreateLivroDTO createLivroDTO){
        livroService.update(id, createLivroDTO);
        return ResponseEntity.noContent().build();
    }
}
