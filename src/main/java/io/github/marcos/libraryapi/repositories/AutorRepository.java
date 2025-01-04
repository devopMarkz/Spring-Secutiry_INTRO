package io.github.marcos.libraryapi.repositories;

import io.github.marcos.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

    Boolean existsByNomeAndDataNascimentoAndNacionalidade(String nome, LocalDate dataNascimento, String nacionalidade);

}
