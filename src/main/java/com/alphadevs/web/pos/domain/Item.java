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
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "item_title", length = 20, nullable = false)
    private String itemTitle;

    @Column(name = "item_barcode")
    private String itemBarcode;

    @NotNull
    @Size(max = 255)
    @Column(name = "item_description", length = 255, nullable = false)
    private String itemDescription;

    @NotNull
    @Column(name = "item_cost", precision=10, scale=2, nullable = false)
    private BigDecimal itemCost;

    @NotNull
    @Column(name = "item_unit_price", precision=10, scale=2, nullable = false)
    private BigDecimal itemUnitPrice;

    @Column(name = "item_reorder_level", precision=10, scale=2)
    private BigDecimal itemReorderLevel;

    @ManyToOne(optional = false)
    @NotNull
    private Product relatedProduct;

    @ManyToOne(optional = false)
    @NotNull
    private Design relatedDesign;

    @ManyToOne(optional = false)
    @NotNull
    private Location itemLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Item itemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public Item itemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
        return this;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public Item itemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
        return this;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Item itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public Item itemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
        return this;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }

    public BigDecimal getItemUnitPrice() {
        return itemUnitPrice;
    }

    public Item itemUnitPrice(BigDecimal itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
        return this;
    }

    public void setItemUnitPrice(BigDecimal itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public BigDecimal getItemReorderLevel() {
        return itemReorderLevel;
    }

    public Item itemReorderLevel(BigDecimal itemReorderLevel) {
        this.itemReorderLevel = itemReorderLevel;
        return this;
    }

    public void setItemReorderLevel(BigDecimal itemReorderLevel) {
        this.itemReorderLevel = itemReorderLevel;
    }

    public Product getRelatedProduct() {
        return relatedProduct;
    }

    public Item relatedProduct(Product product) {
        this.relatedProduct = product;
        return this;
    }

    public void setRelatedProduct(Product product) {
        this.relatedProduct = product;
    }

    public Design getRelatedDesign() {
        return relatedDesign;
    }

    public Item relatedDesign(Design design) {
        this.relatedDesign = design;
        return this;
    }

    public void setRelatedDesign(Design design) {
        this.relatedDesign = design;
    }

    public Location getItemLocation() {
        return itemLocation;
    }

    public Item itemLocation(Location location) {
        this.itemLocation = location;
        return this;
    }

    public void setItemLocation(Location location) {
        this.itemLocation = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        if (item.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", itemCode='" + getItemCode() + "'" +
            ", itemTitle='" + getItemTitle() + "'" +
            ", itemBarcode='" + getItemBarcode() + "'" +
            ", itemDescription='" + getItemDescription() + "'" +
            ", itemCost='" + getItemCost() + "'" +
            ", itemUnitPrice='" + getItemUnitPrice() + "'" +
            ", itemReorderLevel='" + getItemReorderLevel() + "'" +
            "}";
    }
}
