----------- Bank -----------
CREATE TABLE bank
(
    id      BIGSERIAL NOT NULL,
    name    VARCHAR(255) NOT NULL,
    bik     VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

----------- Customer -----------
CREATE TABLE customer
(
    id         BIGSERIAL NOT NULL,
    name       VARCHAR(255) NOT NULL,
    short_name VARCHAR(255),
    address    VARCHAR(255),
    form       VARCHAR(50),
    version    BIGINT NOT NULL DEFAULT 0,
    PRIMARY    KEY (id)
);

----------- deposit -----------
CREATE TABLE deposit
(
    id          BIGSERIAL NOT NULL,
    open_date   DATE NOT NULL,
    percent     NUMERIC(10,2),
    months      INTEGER,
    customer_id BIGINT,
    bank_id     BIGINT,
    version     BIGINT NOT NULL DEFAULT 0,
    PRIMARY     KEY (id),
    CONSTRAINT  fk_bank FOREIGN KEY (bank_id) REFERENCES bank,
    CONSTRAINT  fk_customer FOREIGN KEY (customer_id) REFERENCES customer
);

CREATE INDEX deposit_customer_id_index on deposit(customer_id);
CREATE INDEX deposit_bank_id_index on deposit(bank_id);