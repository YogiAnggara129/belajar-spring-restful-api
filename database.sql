CREATE DATABASE belajar_spring_restful_api;

\c belajar_spring_restful_api;

CREATE TABLE users (
    username VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    token VARCHAR(100) UNIQUE,
    token_expired_date BIGINT
);

