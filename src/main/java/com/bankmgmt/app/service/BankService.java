package com.bankmgmt.app.service;

import com.bankmgmt.app.entity.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private List<Account> accounts = new ArrayList<>();
    private Integer currentId = 1;

    // Method to Create a new Account
    public Account createAccount(String accountHolderName, String accountType, String email) {
        validateAccountType(accountType);
        validateUniqueEmail(email);
        Account account = new Account(currentId++, accountHolderName, accountType, 0.0, email);
        accounts.add(account);
        return account;
    }

    // Method to Get All Accounts
    public List<Account> getAllAccounts() {
        return accounts;
    }

    // Method to Get Account by ID
    public Account getAccountById(Integer id) {
        return accounts.stream()
                .filter(acc -> acc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    // Method to Deposit Money
    public Account deposit(Integer id, Double amount) {
        validateAmount(amount);
        Account account = getAccountById(id);
        account.setBalance(account.getBalance() + amount);
        return account;
    }

    // Method to Withdraw Money
    public Account withdraw(Integer id, Double amount) {
        validateAmount(amount);
        Account account = getAccountById(id);
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        return account;
    }

    // Method to Transfer Money
    public void transfer(Integer fromId, Integer toId, Double amount) {
        validateAmount(amount);
        Account fromAccount = getAccountById(fromId);
        Account toAccount = getAccountById(toId);

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    // Method to Delete Account
    public boolean deleteAccount(Integer id) {
        return accounts.removeIf(account -> account.getId().equals(id));
    }

    // Validation Methods
    private void validateAmount(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private void validateAccountType(String accountType) {
        if (!accountType.equalsIgnoreCase("SAVINGS") && !accountType.equalsIgnoreCase("CURRENT")) {
            throw new IllegalArgumentException("Invalid account type. Must be SAVINGS or CURRENT");
        }
    }

    private void validateUniqueEmail(String email) {
        boolean emailExists = accounts.stream().anyMatch(account -> account.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
