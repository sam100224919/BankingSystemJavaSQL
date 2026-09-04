CREATE DATABASE IF NOT EXISTS banking_system;

USE banking_system;

CREATE TABLE IF NOT EXISTS accounts (
                                        account_id INT PRIMARY KEY,
                                        holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                                            account_id INT NOT NULL,
                                            transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
    );