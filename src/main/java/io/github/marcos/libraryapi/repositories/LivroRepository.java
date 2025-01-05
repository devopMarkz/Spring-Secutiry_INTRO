package io.github.marcos.libraryapi.repositories;

import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    boolean existsByAutor(Autor autor);

    boolean existsByIsbn(String isbn);

}
