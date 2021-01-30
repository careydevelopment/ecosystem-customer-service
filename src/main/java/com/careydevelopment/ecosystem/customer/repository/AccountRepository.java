package com.careydevelopment.ecosystem.customer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.ecosystem.customer.model.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

}
