package com.careydevelopment.ecosystem.customer.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;
import com.careydevelopment.ecosystem.customer.service.CustomerService;
import com.careydevelopment.ecosystem.customer.service.UserService;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {
    
    @Autowired
    private CustomerService customerService;
    
//    @Autowired
//    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserService userService;
    
    private String data = "";
    
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        customerRepository.deleteAll();
//        
        List<Customer> contacts = customerRepository.findAll();
        System.err.println("Size is " + contacts.size());
        
        contacts.forEach(System.err::println);
    }
}