package io.github.marcos.libraryapi.dto.usuario;

import java.util.List;

public record UsuarioDTO(String login, String email, String senha, List<String> roles) {
}
