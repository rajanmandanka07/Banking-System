## Banking System Project

This is a console-based banking system application developed in Java, designed to simulate basic banking operations. The project implements core concepts of Object-Oriented Programming (OOP), Data Structures and Algorithms (DSA), and uses MySQL for database management.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Database Structure](#database-structure)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Project Overview

This project is a console-based banking system developed using Java. It demonstrates Object-Oriented Programming (OOP) principles and provides basic banking functionalities like user registration, account management, fund transfers, and transaction history tracking. The project uses MySQL as the backend database to persist user and transaction data.
```
   src
   └── com
      └── banking
         ├── controller
         │   └── BankingController.java
         ├── dao
         │   ├── AccountDAO.java
         │   ├── TransactionDAO.java
         │   └── UserDAO.java
         ├── exception
         │   ├── InsufficientFundsException.java
         │   ├── InvalidTransactionException.java
         │   └── UserNotFoundException.java
         ├── main
         │   └── Main.java
         ├── model
         │   ├── Account.java
         │   ├── Transaction.java
         │   └── User.java
         ├── service
         │   ├── AccountService.java
         │   ├── TransactionService.java
         │   └── UserService.java
         └── util
             ├── DatabaseConnection.java
             └── InputValidator.java
```

## Features

- **User Registration**:
   - New users can register by providing their personal information.

- **Account Management**:
   - Create a bank account under a registered user.
   - Log in to an existing account using the account number and PIN.
   - Delete existing accounts.
   - View all accounts associated with a user.

- **Fund Management**:
   - **Deposit Funds**: Users can deposit money into their accounts.
   - **Withdraw Funds**: Users can withdraw money from their accounts.
   - **Transfer Funds**: Users can transfer money between accounts securely.

- **Account Details**:
   - View account details including balance, account type, and creation date.
   - View transaction history for an account.
   - Download transaction history as a report.

- **Security**:
   - User login requires an account number and PIN for secure access.
   - All transactions are recorded and timestamped for audit purposes.


## Database Structure

The application uses MySQL with the following database schema:

### Tables

#### users
| Field        | Type              | Attributes                              |
|--------------|-------------------|-----------------------------------------|
| `user_id`    | int               | Primary Key, Auto Increment             |
| `name`       | varchar(255)      | Not Null                                |
| `email`      | varchar(255)      | Not Null, Unique                        |
| `phone_number`| varchar(20)      | Not Null, Unique                        |
| `address`    | text              | Nullable                                |

#### accounts
| Field           | Type              | Attributes                              |
|-----------------|-------------------|-----------------------------------------|
| `account_id`    | int               | Primary Key, Auto Increment             |
| `account_number`| varchar(12)       | Not Null, Unique                        |
| `user_id`       | int               | Nullable, Foreign Key                   |
| `balance`       | decimal(15,2)     | Not Null                                |
| `account_type`  | enum('SAVINGS', 'CURRENT') | Not Null                     |
| `created_at`    | timestamp         | Nullable, Default to CURRENT_TIMESTAMP  |
| `password`      | varchar(255)      | Not Null                                |
| `pin`           | varchar(4)        | Not Null                                |

#### transactions
| Field             | Type              | Attributes                              |
|-------------------|-------------------|-----------------------------------------|
| `transaction_id`  | int               | Primary Key, Auto Increment             |
| `account_id`      | int               | Nullable, Foreign Key                   |
| `account_number`  | varchar(12)       | Nullable, Foreign Key                   |
| `transaction_type`| enum('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') | Not Null   |
| `amount`          | decimal(15,2)     | Not Null                                |
| `transaction_date`| timestamp         | Nullable, Default to CURRENT_TIMESTAMP  |

### Key Points

- **Phone Number as Identifier**: The system uses the phone number for account creation instead of a user ID.
- **Minimal PIN Usage**: Only the source account's PIN is required during fund transfers, simplifying the process while maintaining security.

## Technologies Used

- **Java**: Core language for developing the application.
- **MySQL**: Database management system for storing and managing data.
- **MySQL Workbench 8.0 CE**: Used for database management.
- **OOP Principles**: Implemented throughout the project to ensure modularity and reusability.

## Getting Started

### Prerequisites

- **Java**:
   - Ensure that Java is installed on your machine. You can download it from [Oracle's official website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

- **MySQL**:
   - Set up a MySQL database. You can download MySQL from [MySQL's official website](https://dev.mysql.com/downloads/).

- **MySQL Workbench**:
   - Used for managing the MySQL database, including creating tables and running SQL queries. Download it from [here](https://dev.mysql.com/downloads/workbench/).

- **JDBC Driver**:
   - Ensure the MySQL Connector/J (JDBC Driver) is added to your project to enable communication between your Java application and MySQL database. Download it from [here](https://dev.mysql.com/downloads/connector/j/).

- **iText Library**:
   - The iText library iText 5.5.13.2 is used for PDF operations, such as generating and exporting transaction history reports in PDF format. Add iText to your project dependencies. You can download it from [here](https://github.com/itext/itextpdf/releases/tag/5.5.13.2).

- **IDE**:
   - Use an Integrated Development Environment (IDE) like IntelliJ IDEA, Eclipse, or NetBeans for coding, running, and debugging the Java application.

- **Git**:
   - Git is used for version control. Ensure that Git is installed if you plan to manage your project using GitHub or another version control system. Download Git from [here](https://git-scm.com/).


### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/rajanmandanka07/banking-system-project.git

### Set Up the Database

1. **Create the Database and Tables**
   - Use MySQL Workbench to create the database and tables as per the provided schema.

2. **Configure the Database Connection**
   - Update the database connection settings in the project to match your MySQL configuration.

### Compile and Run the Application

1. **Compile the Application**
   ```bash
   javac -d bin src/com/banking/Main.java
   java -cp bin com.banking.Main

## Usage

- **Account Creation**:
   - Start by creating an account with your phone number, email, and address.

- **Login**:
   - Use your email and password to log in to the system.

- **Deposit/Withdraw Funds**:
   - Perform transactions on your account by depositing or withdrawing money.

- **Fund Transfer**:
   - Transfer money between accounts using the source account's PIN for secure transactions.

- **View Transaction History**:
   - Check past transactions for your account to keep track of your financial activities.

- **View Account Details**:
   - Access detailed information about your account, including balance, account type, and creation date.

- **Delete Account**:
   - If necessary, delete your account securely from the system.

- **View All Accounts**:
   - View all the accounts associated with your user profile.

- **Download Transaction History**:
   - Download a report of your transaction history for record-keeping or audit purposes.

- **Exit Application**:
   - Safely exit the application when you're done by selecting the exit option from the main menu.


## Contributing

Contributions are welcome! Feel free to fork the repository and submit a pull request. Please ensure your changes align with the project's coding standards and contribute positively to the project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any queries, please contact the project creator:

- **Name**: Rajankumar Mandanka
- **GitHub**: [rajanmandanka07](https://github.com/rajanmandanka07)
- **Email**: rajanmandanka07@gmail.com
