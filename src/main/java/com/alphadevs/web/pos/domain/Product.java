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
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 55)
    @Column(name = "product_description", length = 55, nullable = false)
    private String productDescription;

    @NotNull
    @Column(name = "prod_code", nullable = false)
    private String prodCode;

    @Column(name = "profit_perc", precision=10, scale=2)
    private BigDecimal profitPerc;

    @NotNull
    @Column(name = "product_prefix", nullable = false)
    private String productPrefix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Product productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProdCode() {
        return prodCode;
    }

    public Product prodCode(String prodCode) {
        this.prodCode = prodCode;
        return this;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public BigDecimal getProfitPerc() {
        return profitPerc;
    }

    public Product profitPerc(BigDecimal profitPerc) {
        this.profitPerc = profitPerc;
        return this;
    }

    public void setProfitPerc(BigDecimal profitPerc) {
        this.profitPerc = profitPerc;
    }

    public String getProductPrefix() {
        return productPrefix;
    }

    public Product productPrefix(String productPrefix) {
        this.productPrefix = productPrefix;
        return this;
    }

    public void setProductPrefix(String productPrefix) {
        this.productPrefix = productPrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productDescription='" + getProductDescription() + "'" +
            ", prodCode='" + getProdCode() + "'" +
            ", profitPerc='" + getProfitPerc() + "'" +
            ", productPrefix='" + getProductPrefix() + "'" +
            "}";
    }
}
