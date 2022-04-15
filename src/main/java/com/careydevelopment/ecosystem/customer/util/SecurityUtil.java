package com.careydevelopment.ecosystem.customer.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.Contact;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;
import com.careydevelopment.ecosystem.customer.repository.ContactRepository;

@Component
public class SecurityUtil {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountRepository accountRepository;

    public boolean isAuthorizedToAccessContact(String id) {
        boolean authorized = false;

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (username != null) {
            Optional<Contact> existingContactOpt = contactRepository.findById(id);

            if (existingContactOpt.isPresent()) {
                Contact existingContact = existingContactOpt.get();

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
        boolean authorized = false;

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (username != null) {
            Optional<Account> existingAccountOpt = accountRepository.findById(id);

            if (existingAccountOpt.isPresent()) {
                Account existingAccount = existingAccountOpt.get();

                if (existingAccount.getSalesOwner() != null && existingAccount.getSalesOwner().getUsername() != null) {
                    if (username.equals(existingAccount.getSalesOwner().getUsername())) {
                        authorized = true;
                    }
                }
            }
        }

        return authorized;
    }
}
