package foro.alura.apiforo.infra.security;

//DTO creado para que al momento de que devuelva el token generado sea mas legible, y se devuelva en formato json
public record DatosJWTtoken(String jwTtoken) {
}
