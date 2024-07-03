package foro.alura.apiforo.topico;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TopicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idUsuario;
    private String mensaje;
    private String nombreCurso;
    private String titulo;
    private LocalDateTime fechaCreacion;
    private Boolean dudaActiva;


    /*Constructor creado para conversion de datosRegistroTopico a TopicoEnity y poder utilizarlo en el metodo del
    repositorio del controller*/
    public TopicoEntity(DatosRegistroTopico datosRegistroTopico) {
        //Cada que registramos una duda nueva se da por hecho que esta activa
        this.dudaActiva=true;
        this.idUsuario = datosRegistroTopico.idUsuario();
        this.mensaje=datosRegistroTopico.mensaje();
        this.nombreCurso= datosRegistroTopico.nombreCurso();
        this.titulo= datosRegistroTopico.titulo();
        this.fechaCreacion = LocalDateTime.now();
    }

    /*Metodo para actualizar los datos que consideremos, en este caso solo titulo y mensaje, se crea un DTO para este
    metodo y dentro de el se considera si los atributos mensaje y titulo llegan vacios o no*/
    public void actualizarDatos(DatosAactualizarTopico datosAactualizarTopico) {
        if (datosAactualizarTopico.titulo()!=null){
            this.titulo= datosAactualizarTopico.titulo();
        }
        if(datosAactualizarTopico != null){
            this.mensaje= datosAactualizarTopico.mensaje();
        }

    }

    public void desactivaDuda() {
        this.dudaActiva = false;
    }
}
