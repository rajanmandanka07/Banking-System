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

The Banking System project is designed to manage basic banking operations such as account creation, deposits, withdrawals, and fund transfers. The application is entirely console-based and focuses on demonstrating OOP principles in Java.

## Features

- **Account Management**: Create, delete, and manage accounts.
- **User Authentication**: Secure login system using email, password, and PIN.
- **Fund Transfer**: Transfer money between accounts with minimal PIN requirement.
- **Transaction History**: View transaction history for accounts.
- **Data Integrity**: Ensures data consistency by manually deleting related records before user deletion.
- **Database Management**: Uses MySQL for managing user and account information.

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

- **Java**: Ensure that Java is installed on your machine.
- **MySQL**: Set up a MySQL database.
- **MySQL Workbench**: Used for managing the MySQL database.

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

- **Account Creation**: Start by creating an account with your phone number, email, and address.
- **Login**: Use your email and password to log in.
- **Deposit/Withdraw Funds**: Perform transactions on your account.
- **Fund Transfer**: Transfer money between accounts using the source account's PIN.
- **View Transaction History**: Check past transactions for your account.

## Contributing

Contributions are welcome! Feel free to fork the repository and submit a pull request. Please ensure your changes align with the project's coding standards and contribute positively to the project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any queries, please contact the project creator:

- **Name**: Rajan Mandanka
- **GitHub**: [rajanmandanka07](https://github.com/rajanmandanka07)
- **Email**: rajanmandanka07@gmail.com
