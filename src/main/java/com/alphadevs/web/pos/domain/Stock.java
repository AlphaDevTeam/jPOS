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
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "stock_qty", precision=10, scale=2, nullable = false)
    private BigDecimal stockQty;

    @ManyToOne(optional = false)
    @NotNull
    private Location stockLocation;

    @ManyToOne(optional = false)
    @NotNull
    private Item stockItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public Stock stockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
        return this;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public Location getStockLocation() {
        return stockLocation;
    }

    public Stock stockLocation(Location location) {
        this.stockLocation = location;
        return this;
    }

    public void setStockLocation(Location location) {
        this.stockLocation = location;
    }

    public Item getStockItem() {
        return stockItem;
    }

    public Stock stockItem(Item item) {
        this.stockItem = item;
        return this;
    }

    public void setStockItem(Item item) {
        this.stockItem = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stock stock = (Stock) o;
        if (stock.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", stockQty='" + getStockQty() + "'" +
            "}";
    }
}
