package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.model.Client;
import io.github.marcos.libraryapi.repositories.ClientRepository;
import io.github.marcos.libraryapi.services.exceptions.ClientInexistenteException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Client salvar(Client client){
        var senhaCriptografada = passwordEncoder.encode(client.getClientSecret());
        client.setClientSecret(senhaCriptografada);
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client obterPorClientId(String clientId){
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientInexistenteException("Client inexistente."));
    }
}
