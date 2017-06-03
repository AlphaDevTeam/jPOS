package com.alphadevs.web.pos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 20)
    @Column(name = "last_name", length = 20, nullable = false)
    private String lastName;

    @Column(name = "cust_code")
    private String custCode;

    @Column(name = "customer_nic")
    private String customerNIC;

    @Column(name = "credit_limit", precision=10, scale=2)
    private BigDecimal creditLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "vat_number")
    private String vatNumber;

    @ManyToOne(optional = false)
    @NotNull
    private CustomerCategory customerCategory;

    @ManyToOne(optional = false)
    @NotNull
    private Address address;

    @ManyToOne(optional = false)
    @NotNull
    private ContactInfo contactInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCustCode() {
        return custCode;
    }

    public Customer custCode(String custCode) {
        this.custCode = custCode;
        return this;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustomerNIC() {
        return customerNIC;
    }

    public Customer customerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
        return this;
    }

    public void setCustomerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public Customer creditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
        return this;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Customer isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public Customer vatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
        return this;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public CustomerCategory getCustomerCategory() {
        return customerCategory;
    }

    public Customer customerCategory(CustomerCategory customerCategory) {
        this.customerCategory = customerCategory;
        return this;
    }

    public void setCustomerCategory(CustomerCategory customerCategory) {
        this.customerCategory = customerCategory;
    }

    public Address getAddress() {
        return address;
    }

    public Customer address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public Customer contactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
        return this;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", custCode='" + getCustCode() + "'" +
            ", customerNIC='" + getCustomerNIC() + "'" +
            ", creditLimit='" + getCreditLimit() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", vatNumber='" + getVatNumber() + "'" +
            "}";
    }
}
