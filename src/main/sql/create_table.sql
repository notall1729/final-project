create table users(
    id int auto_increment primary key ,
    name varchar(100),
    email varchar(100) unique ,
    password varchar(255)
);

create table emails (
    id varchar(6) primary key ,
    sender_id int,
    subject varchar(255),
    body text,
    sent_at timestamp default current_timestamp,
    foreign key (sender_id) references users(id)
);

create table email_recipients (
    email_id varchar(6),
    recipient_id int,
    is_read boolean default false,
    foreign key (email_id) references emails(id),
    foreign key (recipient_id) references users(id)
);