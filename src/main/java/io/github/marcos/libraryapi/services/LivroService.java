package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.livro.CreateLivroDTO;
import io.github.marcos.libraryapi.dto.livro.LivroResponseDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.model.GeneroLivro;
import io.github.marcos.libraryapi.model.Livro;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import io.github.marcos.libraryapi.repositories.LivroRepository;
import io.github.marcos.libraryapi.repositories.specs.LivroSpecifications;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import io.github.marcos.libraryapi.services.exceptions.LivroDuplicadoException;
import io.github.marcos.libraryapi.services.exceptions.LivroInexistenteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LivroService {

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    @Transactional
    public LivroResponseDTO insert(CreateLivroDTO createLivroDTO){
        Autor autor = autorRepository.findById(createLivroDTO.idAutor())
                .orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        if(livroRepository.existsByIsbn(createLivroDTO.isbn())) throw new LivroDuplicadoException("Livro com esse ISBN já existente.");
        Livro livro = new Livro(null, createLivroDTO.isbn(), createLivroDTO.titulo(), createLivroDTO.dataPublicacao(), createLivroDTO.genero(), createLivroDTO.preco(), autor);
        Livro novoLivro = livroRepository.save(livro);
        AutorResponseDTO autorResponseDTO = AutorResponseDTO.convertToAutorResponseDto(novoLivro.getAutor());
        return new LivroResponseDTO(novoLivro.getId(), novoLivro.getIsbn(), novoLivro.getTitulo(), novoLivro.getDataPublicacao(), novoLivro.getGenero(), novoLivro.getPreco(), autorResponseDTO);
    }

    @Transactional(readOnly = true)
    public LivroResponseDTO findById(String id){
        UUID idLivro = UUID.fromString(id);
        Livro livro = livroRepository.findById(idLivro).orElseThrow(() -> new LivroInexistenteException("Livro inexistente."));
        AutorResponseDTO autorResponseDTO = AutorResponseDTO.convertToAutorResponseDto(livro.getAutor());
        return new LivroResponseDTO(livro.getId(), livro.getIsbn(), livro.getTitulo(), livro.getDataPublicacao(), livro.getGenero(), livro.getPreco(), autorResponseDTO);
    }

    @Transactional
    public void deleteById(String id){
        var idLivro = UUID.fromString(id);
        livroRepository.deleteById(idLivro);
    }

    @Transactional(readOnly = true)
    public List<LivroResponseDTO> findByFilters(String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao){

        Specification<Livro> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if(isbn != null) spec = spec.and(LivroSpecifications.isbnEqual(isbn));
        if(titulo != null) spec = spec.and(LivroSpecifications.tituloLike(titulo));
        if(genero != null) spec = spec.and(LivroSpecifications.generoEqual(genero));
        if(anoPublicacao != null) spec.and(LivroSpecifications.anoPublicacaoEqual(anoPublicacao));
        if(nomeAutor != null) spec.and(LivroSpecifications.nomeAutorLike(nomeAutor));

        List<Livro> livros = livroRepository.findAll(spec).stream().toList();

        return livros.stream().map(livro -> new LivroResponseDTO(livro.getId(), livro.getIsbn(), livro.getTitulo(), livro.getDataPublicacao(), livro.getGenero(), livro.getPreco(), AutorResponseDTO.convertToAutorResponseDto(livro.getAutor()))).toList();
    }

}
