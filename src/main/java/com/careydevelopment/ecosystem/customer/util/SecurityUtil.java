package com.careydevelopment.ecosystem.customer.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;

@Component
public class SecurityUtil {

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private AccountRepository accountRepository;

    public boolean isAuthorizedToAccessContact(String id) {
        boolean authorized = false;

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (username != null) {
            Optional<Customer> existingContactOpt = customerRepository.findById(id);

            if (existingContactOpt.isPresent()) {
                Customer existingContact = existingContactOpt.get();

                if (existingContact.getSalesOwner() != null && existingContact.getSalesOwner().getUsername() != null) {
                    if (username.equals(existingContact.getSalesOwner().getUsername())) {
                        authorized = true;
                    }
                }
            }
        }

        return authorized;
    }

    public boolean isAuthorizedToAccessAccount(String id) {
        boolean authorized = true;

//        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (username != null) {
//            Optional<Account> existingAccountOpt = accountRepository.findById(id);
//
//            if (existingAccountOpt.isPresent()) {
//                Account existingAccount = existingAccountOpt.get();
//
//                if (existingAccount.getSalesOwner() != null && existingAccount.getSalesOwner().getUsername() != null) {
//                    if (username.equals(existingAccount.getSalesOwner().getUsername())) {
//                        authorized = true;
//                    }
//                }
//            }
//        }

        return authorized;
    }
}
