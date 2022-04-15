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
public class AccountValidator {

    @Autowired
    private AccountRepository accountRepository;

    public ErrorResponse validateAccount(Account account) {
        ErrorResponse errorResponse = new ErrorResponse();
        account = (Account) SpaceUtil.trimReflective(account);

        validateAccount(account, errorResponse);

        if (errorResponse.getErrors().size() == 0)
            errorResponse = null;
        return errorResponse;
    }

    private void validateAccount(Account account, ErrorResponse errorResponse) {
        String name = account.getName();

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

    private void addError(ErrorResponse errorResponse, String errorMessage, String field, String code) {
        ValidationError validationError = new ValidationError();
        validationError.setCode(code);
        validationError.setDefaultMessage(errorMessage);
        validationError.setField(field);

        errorResponse.getErrors().add(validationError);
    }
}
