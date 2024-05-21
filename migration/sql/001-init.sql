--liquibase formatted sql

--changeset ovoshchko:001
--runOnChange: true
--rollback DROP TABLE banks;
CREATE TABLE IF NOT EXISTS banks (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL UNIQUE,
    limit_amount BIGINT NOT NULL,

    PRIMARY KEY(id)
);

--changeset ovoshchko:002
--runOnChange: true
--rollback DROP TABLE cards;
CREATE TABLE IF NOT EXISTS cards (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    bank_id BIGINT NOT NULL,
    name TEXT NOT NULL UNIQUE,

    PRIMARY KEY(id),
    FOREIGN KEY (bank_id) REFERENCES banks(id)
);

--changeset ovoshchko:003
--runOnChange: true
--rollback DROP TABLE categories;
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL UNIQUE,

    PRIMARY KEY(id)
);

--changeset ovoshchko:004
--runOnChange: true
--rollback DROP TABLE cashbacks;
CREATE TABLE IF NOT EXISTS cashbacks (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    card_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    percent DECIMAL(7, 4) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,

    PRIMARY KEY(id),
    FOREIGN KEY (card_id) REFERENCES cards(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

--changeset ovoshchko:005
--runOnChange: true
--rollback DROP TABLE transactions;
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    card_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount INT NOT NULL,
    transaction_date TIMESTAMP,

    PRIMARY KEY(id),
    FOREIGN KEY (card_id) REFERENCES cards(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
