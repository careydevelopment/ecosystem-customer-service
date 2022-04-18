package us.careydevelopment.accounting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * A loghtweight version of the Business object from ecosystem-business-service.
 *
 * If the client sends in the ID, then the application will ignore all other properties
 * and retrieve details from the business service.
 *
 * If the client sends in other details but omits the ID, then the details will get embedded
 * as a child document when the parent gets persisted. But the details won't be persisted
 * in the business collection.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Business {

    private String id;

    private BusinessType businessType = BusinessType.BUSINESS;
    private Person person;

    @Size(max = 50, message = "Business name cannot exceed 50 characters")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Business business = (Business) o;
        return Objects.equals(id, business.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
