package foro.alura.apiforo.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    /*Se inyecta esta clase para utilizarla e indicar que se utilice primero el filtro creado por nostros, y asi no
    bloquee todas las demas request*/
    @Autowired
    private SecurityFilter securityFilter;


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf->csrf.disable())
                //Se le indica el tipo de sesion en este caso steteless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Se indica que autorice request
                .authorizeRequests()
                //autorice request del tipo post en especifico del path login
                .requestMatchers(HttpMethod.POST, "/login")
                //por ultimo quetodo este permitido
                .permitAll()
                //con esto decimos que despues del acceso de login, todos los demas deberan ser autenticados
                .anyRequest()
                .authenticated()
                //Se le inidca aqui que tome en cuenta nuestro filtro primero para que no bloquee las demas request
                //la clase valida que el usuario tenga una sesion iniciada
                .and()
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /*Este metodo es creado para que el sistema tenga en su contexto a el objeto del tipo authenticationmanager*/
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /*Con esto hacemos saber que tipo de encriptacion se utilizo para la clave que tenemos en la base de datos*/
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }


}
