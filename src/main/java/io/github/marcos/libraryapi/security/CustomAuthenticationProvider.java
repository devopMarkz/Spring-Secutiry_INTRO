package io.github.marcos.libraryapi.security;

import io.github.marcos.libraryapi.model.Usuario;
import io.github.marcos.libraryapi.services.UsuarioService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Usuario usuario = usuarioService.obterPorLogin(username);

        if(usuario == null) throw new UsernameNotFoundException("Usuário e/ou senha incorretos.");

        if(passwordEncoder.matches(password, usuario.getSenha())){
            return new CustomAuthentication(usuario);
        }

        throw new UsernameNotFoundException("Usuário e/ou senha incorretos.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
