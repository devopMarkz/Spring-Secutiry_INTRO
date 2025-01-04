package io.github.marcos.libraryapi.dto.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdateAutorDTO {

    @NotBlank(message = "Campo obrigatório.")
    private String nome;

    @NotNull(message = "Campo obrigatório.")
    private LocalDate dataNascimento;

    @NotBlank(message = "Campo obrigatório.")
    private String nacionalidade;

    public UpdateAutorDTO(String nome, LocalDate dataNascimento, String nacionalidade) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.nacionalidade = nacionalidade;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }
}
