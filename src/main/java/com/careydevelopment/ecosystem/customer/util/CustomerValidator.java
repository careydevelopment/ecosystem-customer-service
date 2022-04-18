package com.careydevelopment.ecosystem.customer.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.careydevelopment.ecosystem.customer.model.Business;
import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.model.CustomerType;
import com.careydevelopment.ecosystem.customer.model.SalesOwner;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;

import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

@Component
public class CustomerValidator {

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private AccountRepository accountRepository;

    public void validateNewCustomer(final Customer customer, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);
        //contact = (Customer) SpaceUtil.trimReflective(contact);

        if (CustomerType.AGGREGATE.equals(customer.getCustomerType())) {
            validateAggregateOnlyFields(customer, errors);
        }

        validateName(customer, errors);
        validateEmail(customer, errors);
        //validateAccount(contact.getAccount(), errorResponse);
    }

    
    private void validateName(final Customer customer, final List<ValidationError> errors) {
        Customer foundCustomer = customerRepository.findByDisplayNameAndSalesOwnerUsername(customer.getDisplayName(), customer.getSalesOwner().getUsername());
        
        if (foundCustomer != null) {
            ValidationUtil.addError(errors, "Customer with that display name already exists",
                    "displayName", null);
        }
    }
    
    
    private void validateAggregateOnlyFields(final Customer customer, final List<ValidationError> errors) {
        if (customer.isAuthority() != null && !customer.isAuthority()) {
            ValidationUtil.addError(errors, "Aggregate customers must have purchasing authority",
                    "authority", null);
        }
    }
    
    private void validateAccount(Business account, final List<ValidationError> errors) {
//        if (account == null) {
//            addError(errorResponse, "Please include an account", "account", "missingAccount");
//        } else {
//            String name = account.getName();
//
//            if (StringUtils.isBlank(name)) {
//                addError(errorResponse, "Please enter a valid account name", "account", "invalidAccountName");
//            } else {
//            }
//
//            if (StringUtils.isBlank(account.getId())) {
////                Business checkAccount = accountRepository.findByName(name);
////
////                if (checkAccount != null) {
////                    addError(errorResponse, "Account name " + name + " already exists", "account", "accountNameExists");
////                }
//            } else {
////                Optional<Account> checkAccount = accountRepository.findById(account.getId());
////
////                if (checkAccount.isEmpty()) {
////                    addError(errorResponse, "Account with ID " + account.getId() + " does not exist", "account",
////                            "invalidAccountId");
////                }
//            }
//        }
    }

    private void validateEmail(final Customer customer, final List<ValidationError> errors) {
        if (emailExists(customer)) {
            ValidationUtil.addError(errors, "The email you entered already exists",
                    "email", null);
        }
    }

    public boolean emailExists(final Customer customer) {
        boolean exists = false;

        if (!StringUtils.isBlank(customer.getEmail())) {
            Customer contact = customerRepository.findByEmailAndSalesOwnerUsername(customer.getEmail(), customer.getSalesOwner().getUsername());
            exists = (contact != null);
        }

        return exists;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
