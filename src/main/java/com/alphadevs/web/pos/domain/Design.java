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
 * A Design.
 */
@Entity
@Table(name = "design")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "design")
public class Design implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 55)
    @Column(name = "design_description", length = 55, nullable = false)
    private String designDescription;

    @NotNull
    @Column(name = "design_code", nullable = false)
    private String designCode;

    @Column(name = "profit_perc", precision=10, scale=2)
    private BigDecimal profitPerc;

    @NotNull
    @Column(name = "design_prefix", nullable = false)
    private String designPrefix;

    @ManyToOne(optional = false)
    @NotNull
    private Product relatedProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignDescription() {
        return designDescription;
    }

    public Design designDescription(String designDescription) {
        this.designDescription = designDescription;
        return this;
    }

    public void setDesignDescription(String designDescription) {
        this.designDescription = designDescription;
    }

    public String getDesignCode() {
        return designCode;
    }

    public Design designCode(String designCode) {
        this.designCode = designCode;
        return this;
    }

    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }

    public BigDecimal getProfitPerc() {
        return profitPerc;
    }

    public Design profitPerc(BigDecimal profitPerc) {
        this.profitPerc = profitPerc;
        return this;
    }

    public void setProfitPerc(BigDecimal profitPerc) {
        this.profitPerc = profitPerc;
    }

    public String getDesignPrefix() {
        return designPrefix;
    }

    public Design designPrefix(String designPrefix) {
        this.designPrefix = designPrefix;
        return this;
    }

    public void setDesignPrefix(String designPrefix) {
        this.designPrefix = designPrefix;
    }

    public Product getRelatedProduct() {
        return relatedProduct;
    }

    public Design relatedProduct(Product product) {
        this.relatedProduct = product;
        return this;
    }

    public void setRelatedProduct(Product product) {
        this.relatedProduct = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Design design = (Design) o;
        if (design.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), design.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Design{" +
            "id=" + getId() +
            ", designDescription='" + getDesignDescription() + "'" +
            ", designCode='" + getDesignCode() + "'" +
            ", profitPerc='" + getProfitPerc() + "'" +
            ", designPrefix='" + getDesignPrefix() + "'" +
            "}";
    }
}
