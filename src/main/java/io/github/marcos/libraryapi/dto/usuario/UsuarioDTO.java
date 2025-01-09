package io.github.marcos.libraryapi.dto.usuario;

import java.util.List;

public record UsuarioDTO(String login, String senha, List<String> roles) {
}
