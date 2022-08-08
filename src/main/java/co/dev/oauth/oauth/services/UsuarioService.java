package co.dev.oauth.oauth.services;

import co.dev.oauth.oauth.clients.UsuarioFeingClient;
import co.dev.usuarios.commons.models.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UsuarioService implements IUSuarioService, UserDetailsService {
    private Logger log = LoggerFactory.getLogger(UsuarioService.class);
    @Autowired
    private UsuarioFeingClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = client.findByUsrename(username);
        if(usuario == null){
            log.error("Error en el loguin, no existe el usuario "+username+" en el sistema");
            throw new UsernameNotFoundException("Error en el loguin, no existe el usuario "+username+" en el sistema");
        }
        List<GrantedAuthority> authorities= usuario.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .peek(autority -> log.info("Role: "+autority.getAuthority()))
                .collect(Collectors.toList());
        log.info("Usuario Autenticado "+ username);


        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
    //metodo implementado para obtener informacion adicional apra el token, implements de IUSuarioService
    @Override
    public Usuario findByUsername(String username) {
        return client.findByUsrename(username);
    }
}
