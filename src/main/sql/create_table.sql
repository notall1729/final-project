create table users(
    id int auto_increment primary key ,
    name varchar(100),
    email varchar(100) unique ,
    password varchar(255)
);

