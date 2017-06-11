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
 * A CashBook.
 */
@Entity
@Table(name = "cash_book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cashbook")
public class CashBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "cr_amount", precision=10, scale=2, nullable = false)
    private BigDecimal crAmount;

    @NotNull
    @Column(name = "dr_amount", precision=10, scale=2, nullable = false)
    private BigDecimal drAmount;

    @NotNull
    @Column(name = "balance_amount", precision=10, scale=2, nullable = false)
    private BigDecimal balanceAmount;

    @NotNull
    @Column(name = "related_date", nullable = false)
    private LocalDate relatedDate;

    @ManyToOne
    private Location relatedLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public CashBook description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCrAmount() {
        return crAmount;
    }

    public CashBook crAmount(BigDecimal crAmount) {
        this.crAmount = crAmount;
        return this;
    }

    public void setCrAmount(BigDecimal crAmount) {
        this.crAmount = crAmount;
    }

    public BigDecimal getDrAmount() {
        return drAmount;
    }

    public CashBook drAmount(BigDecimal drAmount) {
        this.drAmount = drAmount;
        return this;
    }

    public void setDrAmount(BigDecimal drAmount) {
        this.drAmount = drAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public CashBook balanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public LocalDate getRelatedDate() {
        return relatedDate;
    }

    public CashBook relatedDate(LocalDate relatedDate) {
        this.relatedDate = relatedDate;
        return this;
    }

    public void setRelatedDate(LocalDate relatedDate) {
        this.relatedDate = relatedDate;
    }

    public Location getRelatedLocation() {
        return relatedLocation;
    }

    public CashBook relatedLocation(Location location) {
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
        CashBook cashBook = (CashBook) o;
        if (cashBook.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cashBook.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CashBook{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", crAmount='" + getCrAmount() + "'" +
            ", drAmount='" + getDrAmount() + "'" +
            ", balanceAmount='" + getBalanceAmount() + "'" +
            ", relatedDate='" + getRelatedDate() + "'" +
            "}";
    }
}
