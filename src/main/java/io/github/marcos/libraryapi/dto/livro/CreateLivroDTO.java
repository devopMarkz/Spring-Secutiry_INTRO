package io.github.marcos.libraryapi.dto.livro;

import io.github.marcos.libraryapi.model.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateLivroDTO(
        @ISBN(message = "ISBN inválido")
        @NotBlank(message = "Campo obrigatório.")
        String isbn,
        @NotBlank(message = "Campo obrigatório.")
        String titulo,
        @Past(message = "Não pode ser uma data futura.")
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        @NotNull(message = "Campo obrigatório.")
        UUID idAutor
        ) {
}
