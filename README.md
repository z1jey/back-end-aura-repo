# Banking Management System

A simple Java-based Banking Management System that allows users to manage bank accounts and perform basic banking transactions.

This project was developed to demonstrate Java OOP, JDBC, MySQL database operations, input validation, exception handling, and basic database transaction management.

---

## Description

The Banking Management System is a console-based application connected to a MySQL database.

The application allows an administrator to create and manage bank accounts, perform deposits, withdrawals, and fund transfers, and view account and transaction information.

The system also includes administrator account creation, login, and account archiving through soft delete.

---

## Features

### Admin Features

- Create Admin Account
- Admin Login

### Account Features

- Create Account
- Balance Inquiry
- View Accounts
  - List All Accounts
  - List Active Accounts
  - List Archived Accounts
- Search Account
- Delete Account using Soft Delete

### Transaction Features

- Deposit
- Withdraw
- Fund Transfer
- Transaction History
- Mini Statement

### Validation

- Required fields cannot be empty
- Account numbers must exist
- Account numbers must be unique
- Contact number validation
- Transaction amount must be greater than zero
- Withdrawal cannot exceed the available balance
- Sender and receiver accounts must be different
- Database errors are handled using exceptions

---

## Technologies Used

- **Java**
- **MySQL**
- **JDBC**
- **Maven**
- **SQL**
- **Git / GitHub**
- **IntelliJ IDEA**

---
# Database Setup

The project uses **MySQL** as its database.
The SQL files are located inside the `sql` folder.

```text
sql/
├── schema.sql
├── seed.sql
└── reset.sql
```
## 1. Create the Database
Open MySQL Workbench, phpMyAdmin, or another MySQL client.

Run the `schema.sql` file.

The script creates the database:

```text
banking_db
```

## 2. Run the Seed Data

After running `schema.sql`, run the `seed.sql` file.
The seed file provides sample data that can be used for testing the application.

# Database Configuration

The application uses a properties file for the database connection.

```properties
db.url=
db.username=
db.password=
```

# Running the Application

After the database has been created and the connection has been configured:

1. Open the project in IntelliJ IDEA.
2. Make sure the Maven dependencies are loaded.
3. Make sure MySQL is running.
4. Make sure the `banking_db` database exists.
5. Check the database credentials in the properties file.
6. Run `Main.java`.

The application will start with the login menu.

---


# Educational Purpose

This project was created for educational purposes to demonstrate basic banking operations, Java programming, database connectivity, and database management concepts.
It is not intended to be used as a real banking system.

---
