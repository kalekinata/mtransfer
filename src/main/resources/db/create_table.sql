use bank_client;

create table roles(
      id varchar(36) primary key,
      dadd datetime,
      name varchar(150)
);

set @adm_role := uuid();

insert into roles
values  (uuid(), now(), 'USER'),
        (uuid(), now(), 'BANK'),
        (@adm_role, now(), 'ADMIN');

create table users(
      id varchar(36) primary key,
      dadd datetime,
      username varchar(150),
      email varchar(150),
      password varchar(150),
      active bit
);

set @adm := uuid();

insert into users values(@adm, now(), 'admin', '$2a$10$1fniNjjlA/oqQvvlw0ePX.vQ596cWxkZmqfFnim88m1/uVNhmZEcy' , 1);

create table user_role(
      user_id varchar(36),
      role_id varchar(36),
      constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles(id),
      constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users(id)
);

insert into  user_role values (@adm, @adm_role)
