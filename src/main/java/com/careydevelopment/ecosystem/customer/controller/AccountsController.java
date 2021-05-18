package com.careydevelopment.ecosystem.customer.controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.Contact;
import com.careydevelopment.ecosystem.customer.model.ErrorResponse;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
public class AccountsController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountsController.class);
        
    @Autowired
    private AccountRepository accountRepository;
        
    
    @GetMapping("")
    public ResponseEntity<?> fetchAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        
        LOG.debug("Fetching all accounts for " + username);
        
        if (!StringUtils.isBlank(username)) {
            List<Account> accounts = accountRepository.findBySalesOwnerUsernameOrderByNameAsc(username);
            return ResponseEntity.ok(accounts);
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> fetchAccountById(@PathVariable("id") String id) {
        LOG.debug("Fetching account by id: " + id);
        
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            
            String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (username != null) {
                if (account.getSalesOwner() != null && account.getSalesOwner().getUsername() != null) {
                    if (username.equals(account.getSalesOwner().getUsername())) {
                        return ResponseEntity.ok(accountOpt.get());
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot access that account's info");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem validating account ownership!");
                } 
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem validating contact ownership!");
            }
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
