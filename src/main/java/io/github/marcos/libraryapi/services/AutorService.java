package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.CreateAutorDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import io.github.marcos.libraryapi.services.exceptions.AutorInexistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deleteById(String id){
        var idAutor = UUID.fromString(id);
        Autor autor = autorRepository.findById(idAutor).orElseThrow(() -> new AutorInexistenteException("Autor inexistente."));
        autorRepository.deleteById(autor.getId());
    }

}
