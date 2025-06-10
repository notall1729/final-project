create table User (
    id int primary key auto_increment,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    age int not null,
    email varchar(100) unique not null,
    password varchar(40) not null
);
