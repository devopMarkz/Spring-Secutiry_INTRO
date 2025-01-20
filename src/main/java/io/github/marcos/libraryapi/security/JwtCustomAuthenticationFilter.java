package io.github.marcos.libraryapi.security;

import io.github.marcos.libraryapi.model.Usuario;
import io.github.marcos.libraryapi.services.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof JwtAuthenticationToken){
            String login = authentication.getName();
            Usuario usuario = usuarioService.obterPorLogin(login);
            if(usuario != null){
                authentication = new CustomAuthentication(usuario);
            }
        }

        filterChain.doFilter(request, response);
    }
}
