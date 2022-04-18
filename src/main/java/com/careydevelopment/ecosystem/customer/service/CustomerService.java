package com.careydevelopment.ecosystem.customer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Service;

import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;

@Service
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CustomerRepository customerRepository;

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

    public Customer saveCustomer(Customer Customer) {
        saveAccount(Customer);
        Customer savedCustomer = customerRepository.save(Customer);

        return savedCustomer;
    }

    private void saveAccount(Customer Customer) {
//        String accountId = Customer.getAccount().getId();
//
//        if (accountId == null) {
//            AccountLightweight account = Customer.getAccount();
//            Account accountToSave = AccountUtil.createAccountFromAccountLightweight(account);
//            AccountLightweight savedAccount = (AccountLightweight) accountRepository.save(accountToSave);
//
//            Customer.setAccount(savedAccount);
//        } else {
//            Optional<Account> accountOpt = accountRepository.findById(accountId);
//
//            if (accountOpt.isPresent()) {
//                Customer.setAccount(accountOpt.get());
//            } else {
//                throw new ServiceException("No account with ID: " + accountId, HttpStatus.NOT_FOUND.value());
//            }
//        }
    }
}
