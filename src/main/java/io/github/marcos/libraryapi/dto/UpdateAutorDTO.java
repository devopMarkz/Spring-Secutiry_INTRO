package io.github.marcos.libraryapi.dto;

import java.time.LocalDate;

public class UpdateAutorDTO {

    private String nome;
    private LocalDate dataNascimento;
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
