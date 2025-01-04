package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.CreateAutorDTO;
import io.github.marcos.libraryapi.dto.UpdateAutorDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public AutorResponseDTO insert(CreateAutorDTO createAutorDTO){
        Autor autor = createAutorDTO.mapearParaAutor();
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
    public List<AutorResponseDTO> findByFilters(String nome, String nacionalidade){
        if(nome != null && nacionalidade != null) {
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade).stream()
                    .map(autor -> new AutorResponseDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()))
                    .toList();
        }
        if(nome != null){
            return autorRepository.findByNome(nome).stream()
                    .map(autor -> new AutorResponseDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()))
                    .toList();
        }
        if(nacionalidade != null){
            return autorRepository.findByNacionalidade(nacionalidade).stream()
                    .map(autor -> new AutorResponseDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()))
                    .toList();
        }
        return autorRepository.findAll().stream()
                .map(autor -> new AutorResponseDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()))
                .toList();
    }

    @Transactional
    public void deleteById(String id){
        var idAutor = UUID.fromString(id);
        Autor autor = autorRepository.findById(idAutor).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        autorRepository.deleteById(autor.getId());
    }

    @Transactional
    public void updateById(String id, UpdateAutorDTO updateAutorDTO){
        var idAutor = UUID.fromString(id);
        Autor autor = autorRepository.findById(idAutor).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        autor.setNome(updateAutorDTO.getNome());
        autor.setDataNascimento(updateAutorDTO.getDataNascimento());
        autor.setNacionalidade(updateAutorDTO.getNacionalidade());
        autorRepository.save(autor);
    }

}
