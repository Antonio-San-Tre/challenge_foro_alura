package foro.alura.apiforo.topico;

/*Record creado para mostrar solo datos que necesitamos en nuestro listado de topicos*/

import java.time.LocalDateTime;

public record DatosListadoTopico(Long id, String titulo, String mensaje, LocalDateTime fechaCreacion) {

    /*Constructor creado para la conversion de los datos de nuestra entidad a los datos que mostraremos en el listado de
    nuestro DTO*/
    public DatosListadoTopico(TopicoEntity topicoEntity){
        this(topicoEntity.getId(), topicoEntity.getTitulo(), topicoEntity.getMensaje(), topicoEntity.getFechaCreacion());
    }


}
