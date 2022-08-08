package co.dev.oauth.oauth.services;

import co.dev.usuarios.commons.models.entity.Usuario;

public interface IUSuarioService {
    public Usuario findByUsername(String username);
}
