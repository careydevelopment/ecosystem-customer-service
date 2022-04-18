package com.careydevelopment.ecosystem.customer.util;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Business;
import com.careydevelopment.ecosystem.customer.model.ErrorResponse;
import com.careydevelopment.ecosystem.customer.model.ValidationError;

@Component
public class AccountValidator {

//    @Autowired
//    private AccountRepository accountRepository;

    public ErrorResponse validateAccount(Business account) {
        ErrorResponse errorResponse = new ErrorResponse();
        account = (Business) SpaceUtil.trimReflective(account);

        validateAccount(account, errorResponse);

        if (errorResponse.getErrors().size() == 0)
            errorResponse = null;
        return errorResponse;
    }

    private void validateAccount(Business account, ErrorResponse errorResponse) {
//        String name = account.getName();
//
//        if (StringUtils.isBlank(account.getId())) {
//            Business checkAccount = accountRepository.findByName(name);
//
//            if (checkAccount != null) {
//                addError(errorResponse, "Account name " + name + " already exists", "account", "accountNameExists");
//            }
//        } else {
//            Optional<Account> checkAccount = accountRepository.findById(account.getId());
//
//            if (checkAccount.isEmpty()) {
//                addError(errorResponse, "Account with ID " + account.getId() + " does not exist", "account",
//                        "invalidAccountId");
//            }
//        }
    }

    private void addError(ErrorResponse errorResponse, String errorMessage, String field, String code) {
        ValidationError validationError = new ValidationError();
        validationError.setCode(code);
        validationError.setDefaultMessage(errorMessage);
        validationError.setField(field);

        errorResponse.getErrors().add(validationError);
    }
}
