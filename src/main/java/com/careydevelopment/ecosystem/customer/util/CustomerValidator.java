package com.careydevelopment.ecosystem.customer.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Business;
import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.model.ErrorResponse;
import com.careydevelopment.ecosystem.customer.model.ValidationError;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;

@Component
public class CustomerValidator {

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private AccountRepository accountRepository;

    public ErrorResponse validateCustomer(Customer contact) {
        ErrorResponse errorResponse = new ErrorResponse();
        contact = (Customer) SpaceUtil.trimReflective(contact);

        validateEmail(contact, errorResponse);
        validateAccount(contact.getAccount(), errorResponse);

        if (errorResponse.getErrors().size() == 0)
            errorResponse = null;
        return errorResponse;
    }

    private void validateAccount(Business account, ErrorResponse errorResponse) {
        if (account == null) {
            addError(errorResponse, "Please include an account", "account", "missingAccount");
        } else {
            String name = account.getName();

            if (StringUtils.isBlank(name)) {
                addError(errorResponse, "Please enter a valid account name", "account", "invalidAccountName");
            } else {
            }

            if (StringUtils.isBlank(account.getId())) {
//                Business checkAccount = accountRepository.findByName(name);
//
//                if (checkAccount != null) {
//                    addError(errorResponse, "Account name " + name + " already exists", "account", "accountNameExists");
//                }
            } else {
//                Optional<Account> checkAccount = accountRepository.findById(account.getId());
//
//                if (checkAccount.isEmpty()) {
//                    addError(errorResponse, "Account with ID " + account.getId() + " does not exist", "account",
//                            "invalidAccountId");
//                }
            }
        }
    }

    private void validateEmail(Customer contact, ErrorResponse errorResponse) {
        if (StringUtils.isEmpty(contact.getId()) && emailExists(contact.getEmail())) {
            addError(errorResponse, "The email you entered already exists", "email", "emailExists");
        }
    }

    public boolean emailExists(String email) {
        boolean exists = false;

        if (email != null && !StringUtils.isBlank(email)) {
            Customer contact = customerRepository.findByEmail(email);
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

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
