package io.github.marcos.libraryapi.dto.livro;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.autor.CreateAutorDTO;
import io.github.marcos.libraryapi.model.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LivroResponseDTO(
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        AutorResponseDTO autor
        ) {
}
