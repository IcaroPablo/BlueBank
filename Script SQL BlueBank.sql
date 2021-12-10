create database blue_bank;
use blue_bank;
#drop database blue_bank;

create table client (
id bigint not null auto_increment, 
cep varchar(255), 
cpfcnpj varchar(255), 
email varchar(255), 
name varchar(255), 
password varchar(255), 
phone_number varchar(255), 
status integer, 
type varchar(255),
unique key(cpfcnpj),
primary key (id)
);

create table account (
id bigint not null auto_increment,
account_type integer, 
agency integer not null, 
balance double precision not null, 
date_for_reference datetime, 
status integer, 
client_id bigint, 
primary key (id)
 );
 
 create table transaction (
id bigint not null auto_increment, 
current_balance double precision not null, 
data_agend_transacao datetime,
data_exec_transacao datetime,
previous_balance double precision not null,
transaction_date datetime not null,
transaction_type integer not null,
value double precision not null,
account_id bigint,
destination_account_id bigint,
primary key (id)
);

create table loan (
id bigint not null auto_increment,
borrowed_amount double precision not null,
end_date datetime not null,
fees double precision not null,
installments integer not null,
start_date datetime not null,
client_id bigint,
primary key (id)
);

alter table account add constraint fk_client foreign key (client_id) references client (id);
alter table loan add constraint fk_client2 foreign key (client_id) references client (id);
alter table transaction add constraint fk_account foreign key (account_id) references account (id);
alter table transaction add constraint fk_destination_account foreign key (destination_account_id) references account (id);

## Populando client ##
insert into client
( name, cpfcnpj, email, cep, phone_number, password, type) 
values 
("BlueBank", "02657489781", "bluebank@gmail.com", "55006-700", "(11) 97889-6783","minhasenha123","PJ");

insert into client
( name, cpfcnpj, email, cep, phone_number, password, type) 
values 
("Joao", "15469876325", "joao@gmail.com", "66008-800", "(11) 98670-8227","minhasenha123","PF");

## Populando account ##
insert into account
(account_type, agency, balance, date_for_reference, status, client_id)
values
(0, 1, 5000.0, "2021-12-08 21:00:00", 0, 1);

insert into account
(account_type, agency, balance, date_for_reference, status, client_id)
values
(0, 1, 5000.0, "2021-12-08 21:00:00",0, 2);

## Populando transaction ##
insert into transaction
(current_balance, previous_balance, transaction_date, transaction_type, value, account_id)
values
(4850.0, 5000.0, "2021-12-08 21:30:00", 0, 150.0, 2);

insert into transaction
(current_balance, previous_balance, transaction_date, transaction_type, value, account_id)
values
(5000.0, 4850.0, "2021-12-08 21:35:00", 1, 150.0, 2);

## Populando loan ##
insert into loan
(borrowed_amount, end_date, fees, installments, start_date, client_id)
values
(3000.0, "2022/03/25 21:00:00", 0.2, 3, "21-12-08 21:00:00", 2);

insert into loan
(borrowed_amount, end_date, fees, installments, start_date, client_id)
values
(4000.0, "2022/05/26 21:00:00", 0.2, 3, "22-03-26 21:00:00", 2);