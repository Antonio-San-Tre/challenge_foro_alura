package foro.alura.apiforo.infra.errores;


/*Clase para tratar los errores que se presentan, en este caso tratar el error 500 que aparece al momento de que
* el usuario introduzca o busque un topico en la url que se obtiene */

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErrores {

    /*Con esto podemos atrapar que cuando se lance esta excepcion, se realice lo que se hace en el metodo, se personaliza
    la aplicacion*/
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404 (){
        return ResponseEntity.notFound().build();
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400 (MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }


    private record DatosErrorValidacion (String campo, String error){
        public DatosErrorValidacion (FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }



}
