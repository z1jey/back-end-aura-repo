
-- =============================================================================
-- Banking Management System
-- schema.sql - Database + Table Definitions
-- MySQL 8.0+
--
-- Usage:
--   mysql -u root -p < sql/schema.sql
--
-- Run this file ONCE to create the database and all tables.
-- To rebuild from scratch, run reset.sql first.
-- =============================================================================


-- ---------------------------------------------------------------------------
-- 1. Create the database (safe to run multiple times)
-- ---------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS banking_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE banking_db;


-- ---------------------------------------------------------------------------
-- 2. Disable FK checks while creating tables
-- ---------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;


-- =============================================================================
-- TABLE: users
--
-- Stores the login credentials for users who are allowed to access
-- the Banking Management System.
--
-- For this project, only ONE login account is intended to be used.
-- =============================================================================
CREATE TABLE IF NOT EXISTS users (
                                     user_id      BIGINT          NOT NULL AUTO_INCREMENT
                                     COMMENT 'Unique identifier for the login user',
                                     username     VARCHAR(50)     NOT NULL
COMMENT 'Username used to log in',
password     VARCHAR(255)    NOT NULL
COMMENT 'Password used to log in',

created_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
COMMENT 'Date and time the login user was created',

-- Constraints
CONSTRAINT pk_users          PRIMARY KEY (user_id),
CONSTRAINT uq_users_username UNIQUE (username)

) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci
AUTO_INCREMENT = 1
COMMENT = 'Application login users';


-- =============================================================================
-- TABLE: accounts
--
-- Master record for every bank account.
-- One row per account; balance is kept here as a running total so that
-- balance-inquiry queries never need to aggregate the transactions table.
-- =============================================================================
CREATE TABLE IF NOT EXISTS accounts (
    account_id       BIGINT          NOT NULL AUTO_INCREMENT
                                    COMMENT 'Surrogate primary key',
    account_number   VARCHAR(20)     NOT NULL
                                    COMMENT 'Human-readable unique identifier (e.g. ACC-0001000001)',
    first_name       VARCHAR(50)     NOT NULL
                                    COMMENT 'First name of the account holder',
    last_name        VARCHAR(50)     NOT NULL
                                    COMMENT 'Last name of the account holder',
    contact_number   VARCHAR(20)     NOT NULL
                                    COMMENT 'Contact number of the account holder',
    balance          DECIMAL(15, 2)  NOT NULL DEFAULT 0.00
                                    COMMENT 'Current available balance - always >= 0',
    created_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                    COMMENT 'Row creation timestamp (UTC)',
    updated_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                    ON UPDATE CURRENT_TIMESTAMP
                                    COMMENT 'Last modification timestamp (auto-updated)',

    -- Constraints
    CONSTRAINT pk_accounts          PRIMARY KEY (account_id),
    CONSTRAINT uq_account_number    UNIQUE (account_number),
    CONSTRAINT chk_balance_positive CHECK (balance >= 0)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  AUTO_INCREMENT = 1
  COMMENT = 'Bank account master data';


-- Covers the WHERE clause in every DAO lookup by account number
CREATE INDEX idx_accounts_number
    ON accounts (account_number);
-- Useful for admin queries ordered by creation date
CREATE INDEX idx_accounts_created_at
    ON accounts (created_at);


-- =============================================================================
-- TABLE: transactions
--
-- Append-only ledger - rows are NEVER updated or deleted after insertion.
-- Every financial event produces at least one row.
--
-- A fund transfer produces exactly TWO rows:
--   • TRANSFER_OUT on the sender's account
--   • TRANSFER_IN  on the receiver's account
--
-- Both rows reference their own generated reference_number.
-- The sender's reference_number can appear in the receiver's `remarks`
-- field for traceability.
-- =============================================================================
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id      BIGINT          NOT NULL AUTO_INCREMENT
                                        COMMENT 'Surrogate primary key',

    account_number      VARCHAR(20)     NOT NULL
                                        COMMENT 'Owning account (denormalised for query speed)',

    transaction_type    ENUM(
                            'DEPOSIT',
                            'WITHDRAW',
                            'TRANSFER_IN',
                            'TRANSFER_OUT'
                        )               NOT NULL
                                        COMMENT 'Category of financial event',

    amount              DECIMAL(15, 2)  NOT NULL
                                        COMMENT 'Absolute (positive) monetary amount',
    balance_after       DECIMAL(15, 2)  NOT NULL
                                        COMMENT 'Account balance snapshot immediately after this event',
    reference_number    VARCHAR(30)     NOT NULL
                                        COMMENT 'Globally unique business reference (TXNyyyyMMddHHmmss + seq)',
    remarks             VARCHAR(255)    NULL
                                        COMMENT 'Optional description - e.g. counterparty info for transfers',
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                        COMMENT 'Event timestamp (UTC)',

    -- Constraints
    CONSTRAINT pk_transactions          PRIMARY KEY (transaction_id),
    CONSTRAINT uq_reference_number      UNIQUE (reference_number),
    CONSTRAINT chk_amount_positive      CHECK (amount > 0),
    CONSTRAINT fk_txn_account_number    FOREIGN KEY (account_number)
        REFERENCES accounts (account_number)
        ON DELETE RESTRICT
        ON UPDATE CASCADE

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  AUTO_INCREMENT = 1
  COMMENT = 'Append-only financial event ledger';


-- Primary lookup pattern: all transactions for an account, newest first
CREATE INDEX idx_txn_account_created
    ON transactions (account_number, created_at DESC);

-- Fast reference-number lookup
CREATE INDEX idx_txn_reference_number
    ON transactions (reference_number);

-- Supports filtering by transaction type
CREATE INDEX idx_txn_type
    ON transactions (transaction_type);


-- ---------------------------------------------------------------------------
-- 3. Re-enable FK checks
-- ---------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------------
-- 5. Verification: show created objects
-- ---------------------------------------------------------------------------
SHOW TABLES;

