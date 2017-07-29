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
 * A CashBookBalance.
 */
@Entity
@Table(name = "cash_book_balance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cashbookbalance")
public class CashBookBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cash_balance", precision=10, scale=2, nullable = false)
    private BigDecimal cashBalance;

    @NotNull
    @Column(name = "bank_balance", precision=10, scale=2, nullable = false)
    private BigDecimal bankBalance;

    @ManyToOne(optional = false)
    @NotNull
    private Location relatedLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public CashBookBalance cashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
        return this;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    public CashBookBalance bankBalance(BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
        return this;
    }

    public void setBankBalance(BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
    }

    public Location getRelatedLocation() {
        return relatedLocation;
    }

    public CashBookBalance relatedLocation(Location location) {
        this.relatedLocation = location;
        return this;
    }

    public void setRelatedLocation(Location location) {
        this.relatedLocation = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CashBookBalance cashBookBalance = (CashBookBalance) o;
        if (cashBookBalance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cashBookBalance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CashBookBalance{" +
            "id=" + getId() +
            ", cashBalance='" + getCashBalance() + "'" +
            ", bankBalance='" + getBankBalance() + "'" +
            "}";
    }
}
