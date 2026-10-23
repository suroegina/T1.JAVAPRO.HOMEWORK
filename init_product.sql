create table if not exists products
(
 id bigserial primary key,
 account_number varchar(255) not null unique,
 balance numeric(19,2) not null,
 product_type varchar(50) not null,
 user_id bigint not null,
 constraint fk_product_user foreign key (user_id) references users (id)
);