-- ACCOUNT table
CREATE TABLE account (id INTEGER IDENTITY PRIMARY KEY, iban VARCHAR(24) NOT NULL, name VARCHAR(50) NULL, balance DOUBLE NOT NULL);
INSERT INTO account (iban, name, balance) VALUES('ES9820385778983000760236', 'Sample account 1', 100.20);
INSERT INTO account (iban, name, balance) VALUES('ES0420805237723522472951', 'Sample account 2', 1500.11);
INSERT INTO account (iban, name, balance) VALUES('ES1314656632903771443652', 'Sample account 3', 6500);

-- TRANSACTION table
CREATE TABLE transaction (id INTEGER IDENTITY PRIMARY KEY, reference VARCHAR(10) NULL, iban VARCHAR(24) NOT NULL, date VARCHAR(50) NULL, amount DOUBLE NOT NULL, fee DOUBLE NULL, status VARCHAR(25) NOT NULL, description VARCHAR(50) NULL);

-- TRANSACTION_STATUS table
CREATE TABLE transaction_status (id INTEGER IDENTITY PRIMARY KEY, status VARCHAR(25) NOT NULL);
INSERT INTO transaction_status (status) VALUES ('PENDING');
INSERT INTO transaction_status (status) VALUES ('SETTLED');
INSERT INTO transaction_status (status) VALUES ('INVALID');
INSERT INTO transaction_status (status) VALUES ('FUTURE');