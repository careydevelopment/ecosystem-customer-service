package com.careydevelopment.ecosystem.customer.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.AccountLightweight;
import com.careydevelopment.ecosystem.customer.model.Contact;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;
import com.careydevelopment.ecosystem.customer.repository.ContactRepository;
import com.careydevelopment.ecosystem.customer.service.ContactService;
import com.careydevelopment.ecosystem.customer.service.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {
    
    @Autowired
    private ContactService contactService;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    private String data = "";
    
    public void onApplicationEvent(ApplicationReadyEvent event) {
   }
}