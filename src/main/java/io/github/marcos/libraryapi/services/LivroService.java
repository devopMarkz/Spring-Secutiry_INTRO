package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.livro.CreateLivroDTO;
import io.github.marcos.libraryapi.dto.livro.LivroResponseDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.model.Livro;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import io.github.marcos.libraryapi.repositories.LivroRepository;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public LivroResponseDTO insert(CreateLivroDTO createLivroDTO){
        Autor autor = autorRepository.findById(createLivroDTO.idAutor())
                .orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        Livro livro = new Livro(null, createLivroDTO.isbn(), createLivroDTO.titulo(), createLivroDTO.dataPublicacao(), createLivroDTO.genero(), createLivroDTO.preco(), autor);
        Livro novoLivro = livroRepository.save(livro);
        AutorResponseDTO autorResponseDTO = AutorResponseDTO.convertToAutorResponseDto(novoLivro.getAutor());
        return new LivroResponseDTO(novoLivro.getId(), novoLivro.getIsbn(), novoLivro.getTitulo(), novoLivro.getDataPublicacao(), novoLivro.getGenero(), novoLivro.getPreco(), autorResponseDTO);
    }

}
