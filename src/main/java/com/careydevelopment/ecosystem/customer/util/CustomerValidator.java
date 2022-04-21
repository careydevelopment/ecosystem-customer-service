package com.careydevelopment.ecosystem.customer.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.careydevelopment.ecosystem.customer.exception.InvalidRequestException;
import com.careydevelopment.ecosystem.customer.exception.NotFoundException;
import com.careydevelopment.ecosystem.customer.model.Business;
import com.careydevelopment.ecosystem.customer.model.BusinessType;
import com.careydevelopment.ecosystem.customer.model.Customer;
import com.careydevelopment.ecosystem.customer.model.CustomerType;
import com.careydevelopment.ecosystem.customer.repository.CustomerRepository;
import com.careydevelopment.ecosystem.customer.service.BusinessService;

import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

@Component
public class CustomerValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerValidator.class);

    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BusinessService businessService;
    
    
    public void validateNewCustomer(final Customer customer, final BindingResult bindingResult) throws InvalidRequestException {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);
        //contact = (Customer) SpaceUtil.trimReflective(contact);

        if (CustomerType.AGGREGATE.equals(customer.getCustomerType())) {
            handleAggregate(customer, errors);
        } else if (CustomerType.BUSINESS.equals(customer.getCustomerType())) {
            handleBusiness(customer, errors);
        } else if (CustomerType.KNOWN_INDIVIDUAL.equals(customer.getCustomerType())) {
            handleKnownIndividual(customer, errors);
        }

        validateDisplayName(customer, errors);
        validateEmail(customer, errors);
        
        if (errors.size() > 0) throw new InvalidRequestException("Invalid request", errors);
    }

    private void handleAggregate(final Customer customer, final List<ValidationError> errors) {
        if (StringUtils.isBlank(customer.getDisplayName())) {
            ValidationUtil.addError(errors, "Display name is required",
                    "displayName", null);
        }
    }
    
    private void handleBusiness(final Customer customer, final List<ValidationError> errors) {
        validateBusinessOnlyFields(customer, errors);
    }
    
    private void handleKnownIndividual(final Customer customer, final List<ValidationError> errors) {
        
    }
    
    private void validateDisplayName(final Customer customer, final List<ValidationError> errors) {
        Customer foundCustomer = customerRepository.findByDisplayNameAndSalesOwnerUsername(customer.getDisplayName(), customer.getSalesOwner().getUsername());
        
        if (foundCustomer != null) {
            ValidationUtil.addError(errors, "Customer with that display name already exists",
                    "displayName", null);
        }
    }
    
    private void validateBusinessOnlyFields(final Customer customer, final List<ValidationError> errors) {
        final Business business = customer.getAccount();
        
        if (business == null) {
            ValidationUtil.addError(errors, "Valid business is required for account",
                    "account", null);
            return;
        }   
        
        if (StringUtils.isBlank(business.getId())) {
            ValidationUtil.addError(errors, "Valid account ID is required",
                    "account.id", null);
            return;
        } 
        
        if (!businessExists(business.getId(), customer)) {
            ValidationUtil.addError(errors, "Valid account ID is required",
                    "account.id", null);
        } 
        
        validateBusinessType(customer, errors);
    }    
    
    private boolean businessExists(final String id, Customer customer) {
        boolean found = true;
        
        try {
            final Business business = businessService.fetchBusiness(id);
            
            customer.setAccount(business);
            customer.setDisplayName(business.getDisplayName());
        } catch (NotFoundException ne) {
            LOG.error("Business with ID " + id + " not found");
            found = false;
        }
        
        return found;
    }
    
    private void validateBusinessType(final Customer customer, final List<ValidationError> errors) {
        final BusinessType businessType = customer.getAccount().getBusinessType();
        
        if (BusinessType.INDIVIDUAL.equals(businessType)) {
            if (StringUtils.isBlank(customer.getFirstName())) {
                ValidationUtil.addError(errors, "First name required for individual businesses",
                        "account.firstName", null);
            }
            
            if (StringUtils.isBlank(customer.getLastName())) {
                ValidationUtil.addError(errors, "Last name required for individual businesses",
                        "account.lastName", null);
            }
        }
    }
    
    private void validateEmail(final Customer customer, final List<ValidationError> errors) {
        if (emailExists(customer)) {
            ValidationUtil.addError(errors, "The email you entered already exists",
                    "email", null);
        }
    }

    public boolean emailExists(final Customer customer) {
        boolean exists = false;

        if (!StringUtils.isBlank(customer.getEmail())) {
            Customer contact = customerRepository.findByEmailAndSalesOwnerUsername(customer.getEmail(), customer.getSalesOwner().getUsername());
            exists = (contact != null);
        }

        return exists;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
