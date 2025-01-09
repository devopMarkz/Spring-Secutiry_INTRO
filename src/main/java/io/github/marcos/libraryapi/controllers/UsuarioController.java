package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.usuario.UsuarioDTO;
import io.github.marcos.libraryapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody UsuarioDTO usuarioDTO){
        usuarioService.salvar(usuarioDTO);
        return ResponseEntity.ok().build();
    }

}
