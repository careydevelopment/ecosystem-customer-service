package com.careydevelopment.ecosystem.customer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.ecosystem.customer.model.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Customer findByEmailAndSalesOwnerUsername(String email, String salesOwnerUsername);
    
    Customer findByDisplayNameAndSalesOwnerUsername(String displayName, String salesOwnerUsername);
    
    List<Customer> findBySalesOwnerUsernameOrderByLastNameAsc(String username);

}
