package com.bankmgmt.app.controller;

import com.bankmgmt.app.entity.Account;
import com.bankmgmt.app.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    // Create a new Account
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("accountHolderName");
            String type = request.get("accountType");
            String email = request.get("email");

            Account account = bankService.createAccount(name, type, email);
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(bankService.getAllAccounts());
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(bankService.getAccountById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Deposit Money
    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Integer id, @RequestBody Map<String, Double> request) {
        try {
            return ResponseEntity.ok(bankService.deposit(id, request.get("amount")));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Withdraw Money
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Integer id, @RequestBody Map<String, Double> request) {
        try {
            return ResponseEntity.ok(bankService.withdraw(id, request.get("amount")));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Transfer Money
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Integer fromId, @RequestParam Integer toId, @RequestParam Double amount) {
        try {
            bankService.transfer(fromId, toId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete an account
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        if (bankService.deleteAccount(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
