package foro.alura.apiforo.infra.security;

import foro.alura.apiforo.usuarios.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Hacemos que herede de esta clase abstracta para crear nuestro filtro de las request
@Component
public class SecurityFilter extends OncePerRequestFilter {

    //Inyectamos el tokenservice para poder invocar a la validacion del token
    @Autowired
    private TokenService tokenService;


    @Autowired
    private UsuarioRepository usuarioRepository;


    /*Si se deja tal cual esta el metodo, cuando se levante la aplicacion, y queramos aplicar un request no devolvera
    nada de informacion, se quedara en el filtro y no dejara acceder al controller*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //se obtiene el token del header que ya se aplico en la pestaña de auth seleccionando el bearer
        var authHeader = request.getHeader("Authorization");
        if(authHeader != null) {
            //con esto reemplazara el header de authorization y en vez de decir bearer estara vacio y retornara solo el token
            var token = authHeader.replace("Bearer ", "");
            var subject =tokenService.getSubject(token);

            //si el subject no es nulo el token ya es valido, y tenemo que forzar un inicio de sesion
            if (subject != null){
                var usuario = usuarioRepository.findByLogin(subject);
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

        }
        //Con esto hacemos que pueda acceder al siguiente filtro llamando al request y response
        filterChain.doFilter(request, response);
    }
}


    
