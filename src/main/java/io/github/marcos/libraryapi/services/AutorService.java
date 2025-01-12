package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.autor.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.autor.CreateAutorDTO;
import io.github.marcos.libraryapi.dto.autor.UpdateAutorDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.model.Usuario;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import io.github.marcos.libraryapi.repositories.LivroRepository;
import io.github.marcos.libraryapi.repositories.UsuarioRepository;
import io.github.marcos.libraryapi.services.exceptions.AutorDuplicadoException;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import io.github.marcos.libraryapi.services.exceptions.OperacaoNaoPermitidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AutorService {

    private AutorRepository autorRepository;
    private LivroRepository livroRepository;
    private UsuarioService usuarioService;
    private SecurityService securityService;

    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository, UsuarioService usuarioService, SecurityService securityService) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
        this.usuarioService = usuarioService;
        this.securityService = securityService;
    }

    @Transactional
    public AutorResponseDTO insert(CreateAutorDTO createAutorDTO){
        if (autorRepository.existsByNomeAndDataNascimentoAndNacionalidade(createAutorDTO.nome(), createAutorDTO.dataNascimento(), createAutorDTO.nacionalidade())){
            throw new AutorDuplicadoException("Autor duplicado.");
        }

        Usuario usuario = securityService.obterUsuarioLogado();

        Autor autor = createAutorDTO.mapearParaAutor();
        autor.setIdUsuario(usuario.getId());

        Autor novoAutor = autorRepository.save(autor);
        return new AutorResponseDTO(novoAutor.getId(), novoAutor.getNome(), novoAutor.getDataNascimento(), novoAutor.getNacionalidade());
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO findById(String id){
        var idAutor = UUID.fromString(id);
        Autor autor = autorRepository.findById(idAutor).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        return new AutorResponseDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> pesquisaByExample(String nome, String nacionalidade){
        Autor autor = new Autor(null, nome, null, nacionalidade);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> example = Example.of(autor, exampleMatcher);

        return autorRepository.findAll(example)
                .stream()
                .map(aut -> new AutorResponseDTO(aut.getId(), aut.getNome(), aut.getDataNascimento(), aut.getNacionalidade()))
                .toList();
    }

    @Transactional
    public void deleteById(String id){
        var idAutor = UUID.fromString(id);
        Autor autor = autorRepository.findById(idAutor).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        if(possuiLivro(autor)){
            throw new OperacaoNaoPermitidaException("Erro de integridade referencial.");
        }
        autorRepository.deleteById(autor.getId());
    }

    private boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }

    @Transactional
    public void updateById(String id, UpdateAutorDTO updateAutorDTO){
        var idAutor = UUID.fromString(id);
        if (autorRepository.existsByNomeAndDataNascimentoAndNacionalidade(updateAutorDTO.getNome(), updateAutorDTO.getDataNascimento(), updateAutorDTO.getNacionalidade())){
            throw new AutorDuplicadoException("Autor duplicado.");
        }
        Autor autor = autorRepository.findById(idAutor).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        autor.setNome(updateAutorDTO.getNome());
        autor.setDataNascimento(updateAutorDTO.getDataNascimento());
        autor.setNacionalidade(updateAutorDTO.getNacionalidade());
        autorRepository.save(autor);
    }

}
