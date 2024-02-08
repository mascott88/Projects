CREATE SCHEMA  IF NOT EXISTS java_banking;

-- Table structure 'person' table
CREATE TABLE java_banking.person (
  personemail varchar(50) NOT NULL PRIMARY Key,
  personname varchar(50) NOT NULL,
  personpassword varchar(50) NOT NULL
);

-- Table structure 'accounts' table
CREATE TABLE java_banking.accounts (
  accountnumber serial NOT NULL PRIMARY Key,
  balance float NOT NULL,
  accounttype varchar(20) NOT NULL,
  openingdate varchar(20) NOT NULL,
  personemail varchar(50) NOT NULL,
  accountstatus varchar(20) NOT null
);
ALTER SEQUENCE java_banking.accounts_accountnumber_seq RESTART WITH 1234567890;

-- Table structure 'transactions' table
CREATE TABLE java_banking.transactions (
  accountnumber int NOT NULL,
  transactiondate varchar(20) NOT NULL,
  transactiontype varchar(20) NOT NULL,
  transactionstatus varchar(20) NOT NULL,
  amount float NOT null,
  primary key(accountnumber, transactiondate)
);

-- foreign key constraints
ALTER TABLE java_banking.accounts ADD CONSTRAINT accounts_fk FOREIGN KEY (personemail) REFERENCES java_banking.person(personemail);
ALTER TABLE java_banking.transactions ADD CONSTRAINT transactions_fk FOREIGN KEY (accountnumber) REFERENCES java_banking.accounts(accountnumber);
