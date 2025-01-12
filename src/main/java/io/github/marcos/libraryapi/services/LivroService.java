package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.livro.CreateLivroDTO;
import io.github.marcos.libraryapi.dto.livro.LivroResponseDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.model.GeneroLivro;
import io.github.marcos.libraryapi.model.Livro;
import io.github.marcos.libraryapi.model.Usuario;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import io.github.marcos.libraryapi.repositories.LivroRepository;
import io.github.marcos.libraryapi.repositories.specs.LivroSpecifications;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import io.github.marcos.libraryapi.services.exceptions.LivroDuplicadoException;
import io.github.marcos.libraryapi.services.exceptions.LivroInexistenteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private SecurityService securityService;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository, SecurityService securityService) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.securityService = securityService;
    }

    @Transactional
    public LivroResponseDTO insert(CreateLivroDTO createLivroDTO){
        Autor autor = autorRepository.findById(createLivroDTO.idAutor())
                .orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        if(livroRepository.existsByIsbn(createLivroDTO.isbn())) throw new LivroDuplicadoException("Livro com esse ISBN já existente.");
        Usuario usuarioLogado = securityService.obterUsuarioLogado();
        Livro livro = new Livro(null, createLivroDTO.isbn(), createLivroDTO.titulo(), createLivroDTO.dataPublicacao(), createLivroDTO.genero(), createLivroDTO.preco(), autor);
        livro.setUsuario(usuarioLogado);
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
    public Page<LivroResponseDTO> findByFilters(String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao, Integer pagina, Integer tamanhoPagina){

        Pageable pageable = PageRequest.of(pagina, tamanhoPagina);

        Specification<Livro> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if(isbn != null) spec = spec.and(LivroSpecifications.isbnEqual(isbn));
        if(titulo != null) spec = spec.and(LivroSpecifications.tituloLike(titulo));
        if(genero != null) spec = spec.and(LivroSpecifications.generoEqual(genero));
        if(anoPublicacao != null) spec.and(LivroSpecifications.anoPublicacaoEqual(anoPublicacao));
        if(nomeAutor != null) spec.and(LivroSpecifications.nomeAutorLike(nomeAutor));

        Page<Livro> livros = livroRepository.findAll(spec, pageable);

        return livros.map(livro -> new LivroResponseDTO(livro.getId(), livro.getIsbn(), livro.getTitulo(), livro.getDataPublicacao(), livro.getGenero(), livro.getPreco(), AutorResponseDTO.convertToAutorResponseDto(livro.getAutor())));
    }

    @Transactional
    public void update(String id, CreateLivroDTO updateDTO){
        var idLivro = UUID.fromString(id);

        Autor autor = autorRepository.findById(updateDTO.idAutor()).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        Livro livro = livroRepository.findById(idLivro).orElseThrow(() -> new LivroInexistenteException("Livro inexistente."));

        livro.setIsbn(updateDTO.isbn() == null? livro.getIsbn() : updateDTO.isbn());
        livro.setTitulo(updateDTO.titulo() == null? livro.getTitulo() : updateDTO.titulo());
        livro.setDataPublicacao(updateDTO.dataPublicacao() == null? livro.getDataPublicacao() : updateDTO.dataPublicacao());
        livro.setGenero(updateDTO.genero() == null? livro.getGenero() : updateDTO.genero());
        livro.setPreco(updateDTO.preco() == null? livro.getPreco() : updateDTO.preco());
        livro.setAutor(autor);

        livroRepository.save(livro);
    }

}
