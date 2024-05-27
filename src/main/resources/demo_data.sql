insert into users (name, username, password)
values ('William Emard', 'william.emard@gmailRU.com', '$2a$10$Oe4mD0wlskOKRsE6RHcToe/N1wEPFHYjgeXdofBUDHO1WVs6KscJm'),
       ('Jesica Sauer', 'jesica.sauer@yahooRU.com', '$2a$10$i9O2nBLeyHeIA7RGyB7eC.nyLHmkCwhlB8W5zLG/chg4i7sFJCN9O');

insert into tasks (title, description, status, owner_id, expiration_date)
values ('Buy milk', null, 'TODO', 2, '2024-12-19 10:00:00'),
       ('Call cleaning', 'Negotiate a service', 'IN_PROGRESS', 2, null),
       ('Wake up', null, 'DONE', 2, '2024-01-01 16:30:00'),
       ('Watch parade', 'Turn on TV', 'TODO', 1, '2025-05-09 10:00:00');

insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');
