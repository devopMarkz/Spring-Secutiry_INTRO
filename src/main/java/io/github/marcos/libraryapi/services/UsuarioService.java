package io.github.marcos.libraryapi.services;

import io.github.marcos.libraryapi.dto.usuario.UsuarioDTO;
import io.github.marcos.libraryapi.model.Usuario;
import io.github.marcos.libraryapi.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void salvar(UsuarioDTO usuarioDTO){
        var senha = usuarioDTO.senha();
        var senhaCriptografada = passwordEncoder.encode(senha);
        Usuario usuario = new Usuario(null, usuarioDTO.login(), senhaCriptografada);
        usuarioDTO.roles().forEach(s -> usuario.getRoles().add(s));
        usuarioRepository.save(usuario);
    }

    public Usuario obterPorLogin(String login){
        return usuarioRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente."));
    }

}
