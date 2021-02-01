package com.careydevelopment.ecosystem.customer.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
public class AccountsController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountsController.class);
        
    @Autowired
    private AccountRepository accountRepository;
        
    
    @GetMapping("")
    public ResponseEntity<?> fetchAllAccounts() {
        LOG.debug("Fetching all accounts");
        
        List<Account> accounts = accountRepository.findAllByOrderByNameAsc();
        
        return ResponseEntity.ok(accounts);
    }
}
