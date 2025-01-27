package io.github.marcos.libraryapi.controllers;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.autor.CreateAutorDTO;
import io.github.marcos.libraryapi.dto.autor.UpdateAutorDTO;
import io.github.marcos.libraryapi.services.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/autores")
@Tag(name = "Autores")
public class AutorController implements GenericController {

    private static final Logger log = LoggerFactory.getLogger(AutorController.class);

    @Autowired
    private AutorService autorService;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado")
    })
    public ResponseEntity<Void> salvar(@Valid @RequestBody CreateAutorDTO createAutorDTO){
        log.info("Salvando novo autor:{}", createAutorDTO.nome());
        AutorResponseDTO autorResponseDTO = autorService.insert(createAutorDTO);
        URI location = gerarHeaderLocation(autorResponseDTO.id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do autor pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    })
    public ResponseEntity<AutorResponseDTO> obterDetalhes(@PathVariable("id") String id){
        var autorDTO = autorService.findById(id);
        return ResponseEntity.ok(autorDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Pesquisa um autor por filtros.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso.")
    })
    public ResponseEntity<List<AutorResponseDTO>> pesquisarPorFiltros(@RequestParam(name = "nome", required = false) String nome,
                                                                      @RequestParam(name = "nacionalidade", required = false) String nacionalidade){
        var autores = autorService.pesquisaByExample(nome, nacionalidade);
        return ResponseEntity.ok(autores);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um Autor existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor encontrado."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "400", description = "Autor possui livro cadastrado.")
    })
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") String id){
        autorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza um autor existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Void> atualizarPorId(@PathVariable("id") String id, @Valid @RequestBody UpdateAutorDTO dto){
        autorService.updateById(id, dto);
        return ResponseEntity.noContent().build();
    }

}
