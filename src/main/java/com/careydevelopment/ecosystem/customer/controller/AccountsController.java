package com.careydevelopment.ecosystem.customer.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.ErrorResponse;
import com.careydevelopment.ecosystem.customer.model.SalesOwner;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;
import com.careydevelopment.ecosystem.customer.service.UserService;
import com.careydevelopment.ecosystem.customer.util.AccountValidator;
import com.careydevelopment.ecosystem.customer.util.SecurityUtil;

@RestController
@RequestMapping("/accounts")
public class AccountsController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountsController.class);
        
    @Autowired
    private AccountRepository accountRepository;
        
    @Autowired
    private AccountValidator accountValidator;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SecurityUtil securityUtil;
    
    
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
    
    
    @PostMapping("")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account, HttpServletRequest request) {
        LOG.debug("Creating new account: " + account);
        
        ErrorResponse errorResponse = accountValidator.validateAccount(account);
        if (errorResponse != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        SalesOwner salesOwner = userService.fetchUser(bearerToken);
        account.setSalesOwner(salesOwner);
        
        Account savedAccount = accountRepository.save(account);
        
        return ResponseEntity.ok().body(savedAccount);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") String id, @Valid @RequestBody Account account) {
        LOG.debug("Updating account id: " + id + " with data " + account);
        
        if (securityUtil.isAuthorizedToAccessAccount(id)) {
            if (id == null || id.trim().length() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required");
            } else if (!id.equals(account.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in URL and body don't match");
            }

            ErrorResponse errorResponse = accountValidator.validateAccount(account);
            if (errorResponse != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            } 
            
            Account returnedAccount = accountRepository.save(account); 
            
            return ResponseEntity.ok(returnedAccount);            
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
