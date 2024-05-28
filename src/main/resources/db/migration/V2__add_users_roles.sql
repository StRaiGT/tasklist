create table if not exists users_roles
(
    user_id bigint not null references users(id) on delete cascade,
    role    varchar(255) not null,
    primary key (user_id, role)
);
