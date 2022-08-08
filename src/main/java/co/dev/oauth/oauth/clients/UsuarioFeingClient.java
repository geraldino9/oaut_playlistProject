package co.dev.oauth.oauth.clients;

import co.dev.usuarios.commons.models.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servicio-playlist", url="http://localhost:8080/usuarios/search/")
public interface UsuarioFeingClient {
    @GetMapping("/findUsername")
    public Usuario findByUsrename(@RequestParam String nombre);
}
