package io.github.marcos.libraryapi.repositories;

import io.github.marcos.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

    List<Autor> findByNome(String nome);

    List<Autor> findByNacionalidade(String nacionalidade);

    @Query("SELECT obj FROM Autor obj WHERE UPPER(obj.nome) = UPPER(:nome) AND UPPER(obj.nacionalidade) = UPPER(:nacionalidade)")
    List<Autor> findByNomeAndNacionalidade(@Param("nome") String nome, @Param("nacionalidade") String nacionalidade);

}
