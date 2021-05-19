package com.careydevelopment.ecosystem.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.careydevelopment.ecosystem.customer.model.Account;
import com.careydevelopment.ecosystem.customer.model.AccountLightweight;
import com.careydevelopment.ecosystem.customer.model.Contact;
import com.careydevelopment.ecosystem.customer.repository.AccountRepository;
import com.careydevelopment.ecosystem.customer.repository.ContactRepository;
import com.careydevelopment.ecosystem.customer.util.AccountUtil;

@Service
public class ContactService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactService.class);
	
    
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	
	public List<Contact> findAllContacts() {
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "lastName"); 
		
		Aggregation aggregation = Aggregation.newAggregation(sort);
		
		List<Contact> contacts = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Contact.class), Contact.class).getMappedResults();
		
		return contacts;
	}
	
	
	public Contact saveContact(Contact contact) {
	    saveAccount(contact);
	    Contact savedContact = contactRepository.save(contact);
	    
	    return savedContact;
	}
	
	
	private void saveAccount(Contact contact) {
	    AccountLightweight account = contact.getAccount();
	    Account accountToSave = AccountUtil.createAccountFromAccountLightweight(account);
	    
	    AccountLightweight savedAccount = (AccountLightweight)accountRepository.save(accountToSave);
	    
	    contact.setAccount(savedAccount);
	}
}
