package com.careydevelopment.ecosystem.customer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.ecosystem.customer.model.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    public List<Account> findAllByOrderByNameAsc();
    
    public Account findByName(String name);
    
    public List<Account> findBySalesOwnerUsernameOrderByNameAsc(String username);
}
