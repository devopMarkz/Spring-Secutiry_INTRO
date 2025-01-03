package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.CreateAutorDTO;
import io.github.marcos.libraryapi.services.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody CreateAutorDTO createAutorDTO){
        AutorResponseDTO autorResponseDTO = autorService.insert(createAutorDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorResponseDTO.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> obterDetalhes(@PathVariable("id") String id){
        var autorDTO = autorService.findById(id);
        return ResponseEntity.ok(autorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") String id){
        autorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
