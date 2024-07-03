package foro.alura.apiforo.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import foro.alura.apiforo.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Aqui indicamos que la variable apiscret tome el valor de la variable de ambiente creada en nuestro archivo yml
    @Value("${api.security.secret}")
    private String apiSecret;


    //Para hacerlo dinamico y no aplicar directamente el login y las claves, aplicamos como parametro un objeto Usuario
    public String generarToken (Usuario usuario){
        try {
            //Cambiamos a un algoritmo de hmac para mas facilidad y pide una string que seria la clave de acceso
            /*Para que no apliquemos directamente la clave en el algoritmo, podemos aplicarlo en nuestro archivo yml
            y aplicarlo como variable de ambiente en nuestro computador para reemplazarlo por una variable que se inyecta
            en esta clase*/
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("alura foro")
                    //aqui aplicamos el atributo login de la clase usuario
                    .withSubject(usuario.getLogin())
                    //devolvemos tambien el id del usuario
                    .withClaim("id", usuario.getId())
                    //aplicamos en cuanto tiempo queremos que el token pueda autenticar
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            // creamos esta excepcion para que no se quede vacio
            throw new RuntimeException();
        }
    }


    //recibiremos el token que se genero para la verificacion
    public String getSubject (String token){

        //Llevamos el verifier fuera del try catch
        DecodedJWT verifier = null;
        try {
            //La firma tiene que ser igual al del token generado
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("alura foro")
                    // reusable verifier instance
                    .build()
                    .verify(token);
            verifier.getSubject();

        } catch (JWTVerificationException exception){
            System.out.println(exception.toString());
        }

        if(verifier.getSubject() == null) {
            throw new RuntimeException("Verifier invalido");
        }

        return verifier.getSubject();
    }

    //creamos el metodo para indicar las horas o el tiempo que queremos que dure el token activo
    private Instant generarFechaExpiracion (){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

}
