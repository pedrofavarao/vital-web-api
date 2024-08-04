CREATE TABLE donators (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    gender VARCHAR(1),
    height INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    blood_type VARCHAR(4) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE address (
    id BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(255),
    state VARCHAR(255),
    PRIMARY KEY (id)
);