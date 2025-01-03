package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.AutorResponseDTO;
import io.github.marcos.libraryapi.dto.CreateAutorDTO;
import io.github.marcos.libraryapi.model.Autor;
import io.github.marcos.libraryapi.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
