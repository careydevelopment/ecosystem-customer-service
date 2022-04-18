package com.careydevelopment.ecosystem.customer.util;

import com.careydevelopment.ecosystem.customer.model.Business;

public class AccountUtil {

    public static Business createAccount(Business lw) {
        Business account = new Business();
        account.setId(lw.getId());
        account.setName(lw.getName());
        
        return account;
        
    }
}
