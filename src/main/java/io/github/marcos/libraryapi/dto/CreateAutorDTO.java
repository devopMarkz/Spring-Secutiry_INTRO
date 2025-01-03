package io.github.marcos.libraryapi.dto;

import io.github.marcos.libraryapi.model.Autor;

import java.time.LocalDate;

public record CreateAutorDTO(String nome,
                             LocalDate dataNascimento,
                             String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }

}
