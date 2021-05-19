package com.careydevelopment.ecosystem.customer.model;

import java.math.BigInteger;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Account extends AccountLightweight {

    private Address address;
    private Phone phone;
    private Industry industry;
    
    @Size(max = 256, message = "Account description cannot exceed 256 characters")
    private String description;
    
    private Integer numberOfEmployees;
    
    @Size(max = 8, message = "Stock symbol cannot exceed 8 characters")
    private String stockSymbol;

    @Min(10000)
    private BigInteger annualRevenue;
    
    private AccountStatus status;
    
    private Source source;
    
    private SalesOwner salesOwner;
    
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public Phone getPhone() {
        return phone;
    }
    public void setPhone(Phone phone) {
        this.phone = phone;
    }
    public Industry getIndustry() {
        return industry;
    }
    public void setIndustry(Industry industry) {
        this.industry = industry;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }
    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }
    public String getStockSymbol() {
        return stockSymbol;
    }
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
    public BigInteger getAnnualRevenue() {
        return annualRevenue;
    }
    public void setAnnualRevenue(BigInteger annualRevenue) {
        this.annualRevenue = annualRevenue;
    }
    public AccountStatus getStatus() {
        return status;
    }
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    public Source getSource() {
        return source;
    }
    public void setSource(Source source) {
        this.source = source;
    }
    
    public SalesOwner getSalesOwner() {
        return salesOwner;
    }
    public void setSalesOwner(SalesOwner salesOwner) {
        this.salesOwner = salesOwner;
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
        Account other = (Account) obj;
        if (id == null) {
            if (other.getId() != null)
                return false;
        } else if (!id.equals(other.getId()))
            return false;
        return true;
    }
}
