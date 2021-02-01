package com.careydevelopment.ecosystem.customer.util;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.AccountLightweight;

public class AccountUtil {

    public static Account createAccountFromAccountLightweight(AccountLightweight lw) {
        Account account = new Account();
        account.setId(lw.getId());
        account.setName(lw.getName());
        
        return account;
        
    }
}
