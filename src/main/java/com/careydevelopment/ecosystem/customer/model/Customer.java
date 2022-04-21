package com.careydevelopment.ecosystem.customer.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic customer object.
 * 
 * Used by itself primarily for aggregate sales like "Daily Sales."
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "#{@environment.getProperty('mongo.customer.collection')}")
public class Customer {

    @Id
    private String id;

    @NotNull
    private CustomerType customerType;

    @Size(max = 100, message = "Display name must be between 1 and 100 characters")
    private String displayName;

    private SalesOwner salesOwner;

    private List<String> tags;

    @Email(message = "Please enter a valid email address")
    private String email;
    private List<Phone> phones = new ArrayList<Phone>();
    private List<Address> addresses = new ArrayList<Address>();

    private Source source;

    @Size(max = 50, message = "Source details cannot exceed 50 characters")
    private String sourceDetails;

    private CustomerStatus status;
    private Long statusChange;

    private List<LineOfBusiness> linesOfBusiness;

    @Size(max = 40, message = "Time zone cannot be more than 40 characters")
    private String timezone;

    private Business account;
    
    @Size(max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;
    private Boolean authority;
 
    private Boolean canCall;
    private Boolean canText;
    private Boolean canEmail;

    private String birthdayMonth;
    private Integer birthdayDay;
    
    @Size(max = 512, message = "Notes cannot be more than 512 characters")
    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SalesOwner getSalesOwner() {
        return salesOwner;
    }

    public void setSalesOwner(SalesOwner salesOwner) {
        this.salesOwner = salesOwner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getSourceDetails() {
        return sourceDetails;
    }

    public void setSourceDetails(String sourceDetails) {
        this.sourceDetails = sourceDetails;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public Long getStatusChange() {
        return statusChange;
    }

    public void setStatusChange(Long statusChange) {
        this.statusChange = statusChange;
    }

    public List<LineOfBusiness> getLinesOfBusiness() {
        return linesOfBusiness;
    }

    public void setLinesOfBusiness(List<LineOfBusiness> linesOfBusiness) {
        this.linesOfBusiness = linesOfBusiness;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Business getAccount() {
        return account;
    }

    public void setAccount(Business account) {
        this.account = account;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAuthority() {
        return authority;
    }

    public void setAuthority(Boolean authority) {
        this.authority = authority;
    }

    public Boolean getCanCall() {
        return canCall;
    }

    public void setCanCall(Boolean canCall) {
        this.canCall = canCall;
    }

    public Boolean getCanText() {
        return canText;
    }

    public void setCanText(Boolean canText) {
        this.canText = canText;
    }

    public Boolean getCanEmail() {
        return canEmail;
    }

    public void setCanEmail(Boolean canEmail) {
        this.canEmail = canEmail;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public Integer getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(Integer birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (id == null) {
            if (other.getId() != null)
                return false;
        } else if (!id.equals(other.getId()))
            return false;
        return true;
    }
}
