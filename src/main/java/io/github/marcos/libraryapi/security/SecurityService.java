package io.github.marcos.libraryapi.security;

import io.github.marcos.libraryapi.model.Usuario;
import io.github.marcos.libraryapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    @Autowired
    private UsuarioService usuarioService;

    public Usuario obterUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof CustomAuthentication customAuthentication){
            return customAuthentication.getUsuario();
        }
        return null;
    }

}
