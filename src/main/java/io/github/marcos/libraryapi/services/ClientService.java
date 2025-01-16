package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.model.Client;
import io.github.marcos.libraryapi.repositories.ClientRepository;
import io.github.marcos.libraryapi.services.exceptions.ClientInexistenteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    private Client salvar(Client client){
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client obterPorClientId(String clientId){
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientInexistenteException("Client inexistente."));
    }
}
