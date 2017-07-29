package com.alphadevs.web.pos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CashPaymentVoucher.
 */
@Entity
@Table(name = "cash_payment_voucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cashpaymentvoucher")
public class CashPaymentVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "payment_number", nullable = false)
    private String paymentNumber;

    @Column(name = "payment_ref_number")
    private String paymentRefNumber;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "payment_amount", precision=10, scale=2, nullable = false)
    private BigDecimal paymentAmount;

    @ManyToOne(optional = false)
    @NotNull
    private Location relatedLocation;

    @ManyToOne(optional = false)
    @NotNull
    private Customer relatedCustomer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public CashPaymentVoucher paymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentRefNumber() {
        return paymentRefNumber;
    }

    public CashPaymentVoucher paymentRefNumber(String paymentRefNumber) {
        this.paymentRefNumber = paymentRefNumber;
        return this;
    }

    public void setPaymentRefNumber(String paymentRefNumber) {
        this.paymentRefNumber = paymentRefNumber;
    }

    public String getDescription() {
        return description;
    }

    public CashPaymentVoucher description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public CashPaymentVoucher paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public CashPaymentVoucher paymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Location getRelatedLocation() {
        return relatedLocation;
    }

    public CashPaymentVoucher relatedLocation(Location location) {
        this.relatedLocation = location;
        return this;
    }

    public void setRelatedLocation(Location location) {
        this.relatedLocation = location;
    }

    public Customer getRelatedCustomer() {
        return relatedCustomer;
    }

    public CashPaymentVoucher relatedCustomer(Customer customer) {
        this.relatedCustomer = customer;
        return this;
    }

    public void setRelatedCustomer(Customer customer) {
        this.relatedCustomer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CashPaymentVoucher cashPaymentVoucher = (CashPaymentVoucher) o;
        if (cashPaymentVoucher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cashPaymentVoucher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CashPaymentVoucher{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentRefNumber='" + getPaymentRefNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount='" + getPaymentAmount() + "'" +
            "}";
    }
}
