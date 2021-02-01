package com.careydevelopment.ecosystem.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.careydevelopment.ecosystem.customer.util.PropertiesUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
@EnableCaching
@EnableMongoRepositories(basePackages = {"com.careydevelopment.ecosystem.customer.repository"})
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${mongo.db.name}") 
    private String customerDb;
    
    @Value("${customer.properties.file.location}")
    private String customerPropertiesFile;
    
    @Override
    protected String getDatabaseName() {
        return customerDb;
    }
  
    
    @Override
    @Bean
    public MongoClient mongoClient() {
        PropertiesUtil propertiesUtil = new PropertiesUtil(customerPropertiesFile);
        String connectionString = propertiesUtil.getProperty("mongodb.carey-customer.connection");
        String fullConnectionString = connectionString + "/" + customerDb;
        
        MongoClient client = MongoClients.create(fullConnectionString);
        return client;
    }
}
