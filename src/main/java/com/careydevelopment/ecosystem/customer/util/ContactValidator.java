package com.careydevelopment.ecosystem.customer.util;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.AccountLightweight;
import com.careydevelopment.ecosystem.customer.model.Contact;
import com.careydevelopment.ecosystem.customer.model.ErrorResponse;
import com.careydevelopment.ecosystem.customer.model.ValidationError;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;
import com.careydevelopment.ecosystem.customer.repository.ContactRepository;

@Component
public class ContactValidator {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ErrorResponse validateContact(Contact contact) {
        ErrorResponse errorResponse = new ErrorResponse();
        contact = (Contact) SpaceUtil.trimReflective(contact);

        validateEmail(contact, errorResponse);
        validateAccount(contact.getAccount(), errorResponse);

        if (errorResponse.getErrors().size() == 0)
            errorResponse = null;
        return errorResponse;
    }

    private void validateAccount(AccountLightweight account, ErrorResponse errorResponse) {
        if (account == null) {
            addError(errorResponse, "Please include an account", "account", "missingAccount");
        } else {
            String name = account.getName();

            if (StringUtils.isBlank(name)) {
                addError(errorResponse, "Please enter a valid account name", "account", "invalidAccountName");
            } else {
            }

            if (StringUtils.isBlank(account.getId())) {
                Account checkAccount = accountRepository.findByName(name);

                if (checkAccount != null) {
                    addError(errorResponse, "Account name " + name + " already exists", "account", "accountNameExists");
                }
            } else {
                Optional<Account> checkAccount = accountRepository.findById(account.getId());

                if (checkAccount.isEmpty()) {
                    addError(errorResponse, "Account with ID " + account.getId() + " does not exist", "account",
                            "invalidAccountId");
                }
            }
        }
    }

    private void validateEmail(Contact contact, ErrorResponse errorResponse) {
        if (StringUtils.isEmpty(contact.getId()) && emailExists(contact.getEmail())) {
            addError(errorResponse, "The email you entered already exists", "email", "emailExists");
        }
    }

    public boolean emailExists(String email) {
        boolean exists = false;

        if (email != null && !StringUtils.isBlank(email)) {
            Contact contact = contactRepository.findByEmail(email);
            exists = (contact != null);
        }

        return exists;
    }

    private void addError(ErrorResponse errorResponse, String errorMessage, String field, String code) {
        ValidationError validationError = new ValidationError();
        validationError.setCode(code);
        validationError.setDefaultMessage(errorMessage);
        validationError.setField(field);

        errorResponse.getErrors().add(validationError);
    }

    public ContactRepository getContactRepository() {
        return contactRepository;
    }

    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
}
