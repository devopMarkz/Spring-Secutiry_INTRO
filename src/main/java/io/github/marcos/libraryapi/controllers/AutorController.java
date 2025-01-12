package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.autor.CreateAutorDTO;
import io.github.marcos.libraryapi.dto.autor.UpdateAutorDTO;
import io.github.marcos.libraryapi.services.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController implements GenericController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> salvar(@Valid @RequestBody CreateAutorDTO createAutorDTO){
        AutorResponseDTO autorResponseDTO = autorService.insert(createAutorDTO);
        URI location = gerarHeaderLocation(autorResponseDTO.id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<AutorResponseDTO> obterDetalhes(@PathVariable("id") String id){
        var autorDTO = autorService.findById(id);
        return ResponseEntity.ok(autorDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<List<AutorResponseDTO>> pesquisarPorFiltros(@RequestParam(name = "nome", required = false) String nome,
                                                                      @RequestParam(name = "nacionalidade", required = false) String nacionalidade){
        var autores = autorService.pesquisaByExample(nome, nacionalidade);
        return ResponseEntity.ok(autores);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") String id){
        autorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> atualizarPorId(@PathVariable("id") String id, @Valid @RequestBody UpdateAutorDTO dto){
        autorService.updateById(id, dto);
        return ResponseEntity.noContent().build();
    }

}
