--liquibase formatted sql

--changeset apejkovic:1
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(300) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    role VARCHAR(15) NOT NULL,
    PRIMARY KEY (id)
);

--changeset apejkovic:2
INSERT INTO users VALUES (
    1,
    'admin',
    '$2a$10$MYPv3Gp/qw7rBDbtFsZlRubURNfFYtnJtf7F0GNZf.44ANtDlef.2', //password89A
    'admin@paladin.com',
    'ADMIN'
);
