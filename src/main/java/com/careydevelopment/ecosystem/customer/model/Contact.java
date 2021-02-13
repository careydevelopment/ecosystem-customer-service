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

@Document(collection = "#{@environment.getProperty('mongo.contact.collection')}")
public class Contact {

	@Id
	private String id;
	
	@NotBlank(message = "Please provide a first name")
	@Size(max = 50, message = "First name must be between 1 and 50 characters")
	private String firstName;
	
	@NotBlank(message = "Please provide a last name")
	@Size(max = 50, message = "Last name must be between 1 and 50 characters")
	private String lastName;
	
	@Email(message = "Please enter a valid email address")
	private String email;
	private List<Phone> phones = new ArrayList<Phone>();
	private List<Address> addresses = new ArrayList<Address>();
	
	private Source source;
	
	@Size(max = 50, message = "Source details cannot exceed 50 characters")
	private String sourceDetails;
	
	private ContactStatus status;
	private Long statusChange;
	
	private List<LineOfBusiness> linesOfBusiness;
	
	@Size(max = 50, message = "Title cannot exceed 50 characters")
	private String title;
	private Boolean authority;
	private SalesOwner salesOwner;
	
	@NotNull
	private AccountLightweight account;
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
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


	public ContactStatus getStatus() {
		return status;
	}



	public void setContactStatus(ContactStatus status) {
		this.status = status;
	}

	

	public List<LineOfBusiness> getLinesOfBusiness() {
		return linesOfBusiness;
	}



	public void setLinesOfBusiness(List<LineOfBusiness> linesOfBusiness) {
		this.linesOfBusiness = linesOfBusiness;
	}


	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public Boolean isAuthority() {
		return authority;
	}



	public void setAuthority(Boolean authority) {
		this.authority = authority;
	}
	

	public SalesOwner getSalesOwner() {
		return salesOwner;
	}



	public void setSalesOwner(SalesOwner salesOwner) {
		this.salesOwner = salesOwner;
	}


	public Long getStatusChange() {
		return statusChange;
	}



	public void setStatusChange(Long statusChange) {
		this.statusChange = statusChange;
	}
	

    public AccountLightweight getAccount() {
        return account;
    }



    public void setAccount(AccountLightweight account) {
        this.account = account;
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
        Contact other = (Contact) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    
}
