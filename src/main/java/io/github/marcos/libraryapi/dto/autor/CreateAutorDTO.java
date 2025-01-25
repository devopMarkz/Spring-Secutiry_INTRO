package io.github.marcos.libraryapi.dto.autor;

import io.github.marcos.libraryapi.model.Autor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(name = "Autor")
public record CreateAutorDTO(
        @Schema(name = "nome")
        @NotBlank(message = "Campo obrigatório.")
        String nome,

        @Schema(name = "dataNascimento")
        @NotNull(message = "Campo obrigatório.")
        LocalDate dataNascimento,

        @Schema(name = "nacionalidade")
        @NotBlank(message = "Campo obrigatório.")
        String nacionalidade
) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }

}
