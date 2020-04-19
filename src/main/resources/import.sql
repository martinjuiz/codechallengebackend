-- CHANNEL table
CREATE TABLE channel (id INTEGER IDENTITY PRIMARY KEY, name VARCHAR(25) NOT NULL);
INSERT INTO channel (name) VALUES ('CLIENT');
INSERT INTO channel (name) VALUES ('ATM');
INSERT INTO channel (name) VALUES ('INTERNAL');

-- ACCOUNT table
CREATE TABLE account (id INTEGER IDENTITY PRIMARY KEY, iban VARCHAR(24) NOT NULL, name VARCHAR(50) NULL, balance DOUBLE NOT NULL, balance_inc_pending DOUBLE NULL, overdraft DOUBLE NULL, pending DOUBLE NULL);
INSERT INTO account (iban, name, balance, balance_inc_pending, overdraft, pending) VALUES('ES9820385778983000760236', 'Sample account 1', 100.20, 100.20, 1000, 0);
INSERT INTO account (iban, name, balance, balance_inc_pending, overdraft, pending) VALUES('ES0420805237723522472951', 'Sample account 2', 1500.11, 1500.11, 500, 0);
INSERT INTO account (iban, name, balance, balance_inc_pending, overdraft, pending) VALUES('ES1314656632903771443652', 'Sample account 3', 6500, 6500, 10000, 0);

-- TRANSACTION table
CREATE TABLE transaction (id INTEGER IDENTITY PRIMARY KEY, reference VARCHAR(10) NULL, iban VARCHAR(24) NOT NULL, date VARCHAR(50) NULL, amount DOUBLE NOT NULL, fee DOUBLE NULL, status VARCHAR(25) NOT NULL, description VARCHAR(50) NULL);

-- TRANSACTION_STATUS table
CREATE TABLE transaction_status (id INTEGER IDENTITY PRIMARY KEY, status VARCHAR(25) NOT NULL);
INSERT INTO transaction_status (status) VALUES ('PENDING');
INSERT INTO transaction_status (status) VALUES ('SETTLED');
INSERT INTO transaction_status (status) VALUES ('INVALID');
INSERT INTO transaction_status (status) VALUES ('FUTURE');