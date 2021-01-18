package com.careydevelopment.contact.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.careydevelopment.contact.model.Contact;
import com.careydevelopment.contact.model.Source;

@Service
public class ContactService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactService.class);
	
    
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public List<Contact> findAllContacts() {
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "lastName"); 
		
		Aggregation aggregation = Aggregation.newAggregation(sort);
		
		List<Contact> contacts = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Contact.class), Contact.class).getMappedResults();
		
		return contacts;
	}
	
	
	public List<Contact> findContactsBySource(Source source, long maxDocuments) {
		AggregationOperation match = Aggregation.match(Criteria.where("source").is(source));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "lastName"); 
		AggregationOperation limit = Aggregation.limit(maxDocuments);
		
		Aggregation aggregation = Aggregation.newAggregation(match, sort, limit);
		
		List<Contact> contacts = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Contact.class), Contact.class).getMappedResults();
		
		return contacts;
	}
	
	
	public List<ContactInfo> countContactsBySource() {
		AggregationOperation group = Aggregation.group("source").count().as("count");
		AggregationOperation project = Aggregation.project("count").and("source").previousOperation();
		
		Aggregation aggregation = Aggregation.newAggregation(group, project);

		List<ContactInfo> contactInfo = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Contact.class), ContactInfo.class).getMappedResults();
		
		return contactInfo;
	}
	
	
	public List<Contact> findDistinctSourceValues() {
		AggregationOperation group = Aggregation.group("source");
		
		Aggregation aggregation = Aggregation.newAggregation(group);

		List<Contact> contacts = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Contact.class), Contact.class).getMappedResults();
		
		return contacts;
	}
	
	
	public List<ContactInfo> groupContactsBySource() {
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "lastName"); 
		AggregationOperation fullName = Aggregation.project("source").and("firstName").concat(" ", Aggregation.fields("lastName")).as("fullName");
		AggregationOperation group = Aggregation.group("source").push("fullName").as("contacts");
		AggregationOperation project = Aggregation.project("contacts").and("source").previousOperation();
		
		Aggregation aggregation = Aggregation.newAggregation(sort, fullName, group, project);

		List<ContactInfo> contactInfo = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Contact.class), ContactInfo.class).getMappedResults();
		
		return contactInfo;
	}
	
	
	public static class ContactInfo {
		private Source source;
		private Long count;
		private List<String> contacts;
		
		public Source getSource() {
			return source;
		}
		public void setSource(Source source) {
			this.source = source;
		}
		public Long getCount() {
			return count;
		}
		public void setCount(Long count) {
			this.count = count;
		}
		public List<String> getContacts() {
			return contacts;
		}
		public void setContacts(List<String> contacts) {
			this.contacts = contacts;
		}		
	}
}