package io.github.marcos.libraryapi.dto.autor;

import io.github.marcos.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorResponseDTO(UUID id, String nome, LocalDate dataNascimento, String nacionalidade) {

    public static AutorResponseDTO convertToAutorResponseDto(Autor autor){
        return new AutorResponseDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());
    }

}
