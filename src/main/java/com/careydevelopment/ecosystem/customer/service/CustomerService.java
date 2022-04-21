package com.careydevelopment.ecosystem.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.careydevelopment.ecosystem.customer.exception.InvalidRequestException;
import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.model.CustomerType;
import com.careydevelopment.ecosystem.customer.model.SalesOwner;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;
import com.careydevelopment.ecosystem.customer.util.CustomerValidator;

@Service
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerValidator customerValidator;
    
//    @Autowired
//    private AccountRepository accountRepository;

    public List<Customer> findAllCustomers() {
        AggregationOperation sort = Aggregation.sort(Direction.ASC, "lastName");
        Aggregation aggregation = Aggregation.newAggregation(sort);

        List<Customer> Customers = mongoTemplate
                .aggregate(aggregation, mongoTemplate.getCollectionName(Customer.class), Customer.class)
                .getMappedResults();

        return Customers;
    }

    /**
     * Takes the incoming data and makes sure that only the data relevant
     * to the customer type gets persisted.
     */
    private Customer sanitize(final Customer customer, boolean isNew) {
        final SalesOwner salesOwner = userService.fetchUser();        
        final Customer customerToPersist = new Customer();
        
        if (CustomerType.AGGREGATE.equals(customer.getCustomerType())) {
            customerToPersist.setDisplayName(customer.getDisplayName());
            customerToPersist.setSalesOwner(salesOwner);
        } else if (CustomerType.BUSINESS.equals(customer.getCustomerType())) {
            BeanUtils.copyProperties(customer, customerToPersist);
            customerToPersist.setFirstName(null);
            customerToPersist.setLastName(null);
            customerToPersist.setAuthority(null);
            customerToPersist.setBirthdayDay(null);
            customerToPersist.setBirthdayMonth(null);
            customerToPersist.setCanCall(null);
            customerToPersist.setCanEmail(null);
            customerToPersist.setCanText(null);
            customerToPersist.setAddresses(new ArrayList<>());
            customerToPersist.setPhones(new ArrayList<>());
        } else if (CustomerType.KNOWN_INDIVIDUAL.equals(customer.getCustomerType())) {
            BeanUtils.copyProperties(customer, customerToPersist);
            customerToPersist.setAccount(null);
        } else if (CustomerType.INDIVIDUAL_AND_BUSINESS.equals(customer.getCustomerType())) {
            BeanUtils.copyProperties(customer, customerToPersist);
        }
        
        customerToPersist.setId(isNew ? null : customer.getId());
        customerToPersist.setSalesOwner(salesOwner);
        
        return customerToPersist;
    }
    
    public Customer saveNewCustomer(final Customer customer, final BindingResult bindingResult) throws InvalidRequestException {
        final Customer customerToPersist = sanitize(customer, true);

        customerValidator.validateNewCustomer(customerToPersist, bindingResult);
        
        Customer savedCustomer = customerRepository.save(customerToPersist);

        return savedCustomer;
    }
}
