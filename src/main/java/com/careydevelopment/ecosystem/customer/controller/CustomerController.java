package com.careydevelopment.ecosystem.customer.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.model.SalesOwner;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;
import com.careydevelopment.ecosystem.customer.service.CustomerService;
import com.careydevelopment.ecosystem.customer.service.UserService;
import com.careydevelopment.ecosystem.customer.util.CustomerValidator;
import com.careydevelopment.ecosystem.customer.util.SecurityUtil;

@RestController
public class CustomerController {
    
    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
        

    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerValidator customerValidator;
    
    @Autowired
    private SecurityUtil securityUtil;
    
    
    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody final Customer contact, final BindingResult bindingResult, final HttpServletRequest request) {
        LOG.debug("Creating new contact: " + contact);
        
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final SalesOwner salesOwner = userService.fetchUser(bearerToken);
        contact.setSalesOwner(salesOwner);
        
        customerValidator.validateNewCustomer(contact, bindingResult);
                
        Customer savedCustomer = customerService.saveCustomer(contact);
        
        return ResponseEntity.ok().body(savedCustomer);
    }
    
    
    @GetMapping("/{id}") 
    public ResponseEntity<?> fetchCustomer(@PathVariable("id") String id, HttpServletRequest request) {
        LOG.debug("Fetching contact by id: " + id);
    
        String ipAddress = request.getRemoteAddr() + ":" + request.getRemotePort();
        LOG.debug("Remote IP address is " + ipAddress);

        
        if (securityUtil.isAuthorizedToAccessContact(id)) {
            Optional<Customer> contactOpt = customerRepository.findById(id);
            
            if (contactOpt.isPresent()) {
                return ResponseEntity.ok(contactOpt.get());                
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") String id, @Valid @RequestBody Customer contact) {
        LOG.debug("Updating contact id: " + id + " with data " + contact);
        
        if (securityUtil.isAuthorizedToAccessContact(id)) {
            Optional<Customer> existingCustomerOpt = customerRepository.findById(id);
            
            if (existingCustomerOpt.isPresent()) {
                if (id == null || id.trim().length() == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required");
                } else if (!id.equals(contact.getId())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in URL and body don't match");
                }

//                ErrorResponse errorResponse = customerValidator.validateCustomer(contact);
//                if (errorResponse != null) {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//                }
                
                Customer newCustomer = customerService.saveCustomer(contact); 
                
                return ResponseEntity.ok(newCustomer);           
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot access that contact's info");            
        }
    }

    
    @GetMapping("")
    public ResponseEntity<?> fetchCustomers(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        LOG.debug("Remote IP address is " + ipAddress);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        
        LOG.debug("Fetching all contacts for " + username);
        
        if (!StringUtils.isBlank(username)) {
            List<Customer> contacts = customerRepository.findBySalesOwnerUsernameOrderByLastNameAsc(username);
            return ResponseEntity.ok(contacts);
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    
    @PostMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestBody Map<String, Object> inputData) {
        String email = (String)inputData.get("email");
        LOG.debug("Checking for existence of email " + email);
        
        //Boolean bool = customerValidator.emailExists(email);
        
        return ResponseEntity.status(HttpStatus.OK).body(true); 
    }
}
