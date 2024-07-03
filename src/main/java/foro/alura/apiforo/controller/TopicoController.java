package foro.alura.apiforo.controller;


import foro.alura.apiforo.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/dudapararesolver")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    public ResponseEntity<DatosListadoTopico> registrarDuda (@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                         UriComponentsBuilder uriComponentsBuilder){
        //Dentro del constructor que se crea en nuestra entidad podemos hacer la conversion de datosresgistrotopico
        TopicoEntity topico = repository.save(new TopicoEntity(datosRegistroTopico));
        DatosListadoTopico datosListadoTopico = new DatosListadoTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion());

        /*Se crea un objeto del tipo URI, para poder devolver la url donde esta registrado el topico para hacerlo
        dinamico, su path lo volvemos de esa manera, y al ultimo, retornamos el metodo created junto con el cuerpo
        de nuestro record*/
        URI uri = uriComponentsBuilder.path("/dudapararesolver/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(datosListadoTopico);

    }

    /*Lo que retorna es una pagina para el frontend, este metodo devolvera un objeto del tipo PAGE, y recibira parametros
    Pageable y su objeto va como parametro en el metodo findall*/
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarDuadas (@PageableDefault(size = 6) Pageable paginacion) {
        //Retornara todos los registros que estan en la base de datos sin importar cualquier cosa
        //return repository.findAll(paginacion).map(DatosListadoTopico::new);
        return ResponseEntity.ok(repository.findByDudaActivaTrue(paginacion).map(DatosListadoTopico::new));
    }


    /*Si no se aplican las anotaciones requestBody y Valid, spring dara por hecho que los parametros recibidos son
    nulos y no actualizara nada, al igual que es necesario aplicar transactional, para que pueda actualizar los datos*/
    @PutMapping
    @Transactional
    public ResponseEntity actualizarTopico(@RequestBody @Valid DatosAactualizarTopico datosAactualizarTopico){
        TopicoEntity topico = repository.getReferenceById(datosAactualizarTopico.id());
        topico.actualizarDatos(datosAactualizarTopico);
        //Retornamos el objeto actualizado, pero tenemos que crear otro record para que no se muestren los datos de la entidad
        return ResponseEntity.ok(new DatosRespuestaActualizada(topico.getId(), topico.getTitulo(), topico.getMensaje()));
    }

    //Agregamos la variable dinamica que se le indicara para identificar que es lo que borrara
    //al igual que el parametro que recibira se le indica que tipo es para que lo identifique como pathvariable
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        TopicoEntity topico = repository.getReferenceById(id);
        //Dentro del parametro de este metodo ira la variable que almacena el tema que queremos borrar definitivamente
        //repository.delete(topico);
        //Hacemos que retorne un responseentity y la respuesta 204 no content
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoTopico> retornaTopico (@PathVariable Long id){
        TopicoEntity topico = repository.getReferenceById(id);
        var datosTopico = new DatosListadoTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion());

        return ResponseEntity.ok(datosTopico);

    }

}
