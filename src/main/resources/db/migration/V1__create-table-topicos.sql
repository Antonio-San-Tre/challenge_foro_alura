create table topicos(

    id bigint not null auto_increment,
    idUsuario varchar(100) not null,
    mensaje varchar(100) not null,
    nombreCurso varchar(100) not null,
    titulo varchar(100) not null,

    primary key(id)

);