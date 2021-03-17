package com.careydevelopment.ecosystem.customer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.careydevelopment.ecosystem.customer.model.Contact;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {

    public Contact findByEmail(String email);
    
    public List<Contact> findBySalesOwnerUsernameOrderByLastNameAsc(String username);

}
