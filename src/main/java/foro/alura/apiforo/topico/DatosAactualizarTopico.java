package foro.alura.apiforo.topico;


import jakarta.validation.constraints.NotNull;

/*Record para actualizar los datos en este caso se eligio el titulo y mensaje, se necesita del id unico que crea la base
de datos para poder identificar que usuario es el que vamos a hacer los cambios */
public record DatosAactualizarTopico(@NotNull Long id, String titulo, String mensaje) {
}
