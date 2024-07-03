package foro.alura.apiforo.controller;

import foro.alura.apiforo.infra.security.DatosJWTtoken;
import foro.alura.apiforo.infra.security.TokenService;
import foro.alura.apiforo.usuarios.DatosAutenticacionUsuario;
import foro.alura.apiforo.usuarios.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {


    /*Esta clase permite registrar usuarios, autenticarlos, obtener los usuarios autenticados, pero se necesita que
    java lo tenga contemplado en su sistema, ya que este no lo reconoce, iremos a la clase de configuraciones de seguridad*/
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario (@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){

        Authentication authToken= new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.login(),
                datosAutenticacionUsuario.clave());
        //creamos la variable para poder utilizarla y castearla en el parametro de nuestro metodo
        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        //guardamos en una variable la generacion del token y aplicamos la variable usuarioautenticado
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        //Lo retornamos en el cuerpo de la respuesta en insomnia, despues creamos un DTO para los datos que queremos
        //devolver
        return ResponseEntity.ok(new DatosJWTtoken(JWTtoken));

    }


}
