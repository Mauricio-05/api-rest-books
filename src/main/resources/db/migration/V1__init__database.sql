CREATE sequence author_id_seq start WITH 1 increment BY 50;

CREATE TABLE IF NOT EXISTS author (
    id BIGINT NOT NULL,
    name VARCHAR(255),
    age INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS book (
    isbn VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    author_id BIGINT,
    PRIMARY KEY (isbn)
);

ALTER TABLE if EXISTS book ADD CONSTRAINT fk_book_author_id FOREIGN KEY (author_id) REFERENCES author;
