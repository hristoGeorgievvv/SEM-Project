package nl.tudelft.sem.sem54.fridge.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner;

    @Column(nullable = false)
    private int portions;

    @Column(nullable = false, name = "product_name", unique = true, length = 191)
    private String productName;

    @Column(nullable = false, name = "portions_left")
    private int portionsLeft;

    @Column(nullable = false, name = "credit_value")
    private int creditValue;

    @Column(nullable = false, name = "expiration_date")
    private Date expirationDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Fridge fridge;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Column(name = "transaction_list")
    private Collection<ProductTransaction> transactionList;

    public Product() {
    }


    /**
     * Constructor for testing purposes.
     *
     * @param productName    product name
     * @param portions       portions
     * @param portionsLeft   portionsLeft
     * @param creditValue    credit value of product
     * @param expirationDate expirationDate
     * @param fridge         fridge object
     */
    public Product(String productName,
                   int portions, int portionsLeft, int creditValue,
                   Date expirationDate, Fridge fridge) {
        this.productName = productName;
        this.portions = portions;
        this.portionsLeft = portionsLeft;
        this.creditValue = creditValue;
        this.expirationDate = expirationDate;
        this.fridge = fridge;
        this.transactionList = new ArrayList<>();
    }

    /**
     * Constructor for creating a new Product.
     *
     * @param productName    product name
     * @param owner          product owner
     * @param portions       portions
     * @param creditValue    credit value of product
     * @param expirationDate expirationDate
     * @param fridge         fridge object
     */
    public Product(String productName, User owner,
                   int portions, int creditValue,
                   Date expirationDate, Fridge fridge) {
        this.productName = productName;
        this.owner = owner;
        this.portions = portions;
        this.portionsLeft = portions;
        this.creditValue = creditValue;
        this.expirationDate = expirationDate;
        this.fridge = fridge;
        this.transactionList = new ArrayList<>();
    }



    public int getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(int creditValue) {
        this.creditValue = creditValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPortions() {
        return portions;
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
        return portions == product.portions
                && portionsLeft == product.portionsLeft
                && Objects.equals(expirationDate, product.expirationDate)
                && Objects.equals(fridge, product.fridge)
                && Objects.equals(productName, product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, portions, portionsLeft, expirationDate, fridge);
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public int getPortionsLeft() {
        return portionsLeft;
    }

    public void setPortionsLeft(int portionsLeft) {
        this.portionsLeft = portionsLeft;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Fridge getFridge() {
        return fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public Collection<ProductTransaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<ProductTransaction> transactionList) {
        this.transactionList = transactionList;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public static class Builder {
        private User owner;
        private int portions;
        private int portionsLeft;
        private int creditValue;
        private String productName;
        private Date expirationDate;
        private Fridge fridge;

        public Builder setOwner(User owner) {
            this.owner = owner;
            return this;
        }

        public Builder setPortions(int portions) {
            this.portions = portions;
            return this;
        }

        public Builder setPortionsLeft(int portionsLeft) {
            this.portionsLeft = portionsLeft;
            return this;
        }

        public Builder setCreditValue(int creditValue) {
            this.creditValue = creditValue;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder setFridge(Fridge fridge) {
            this.fridge = fridge;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    private Product(Builder b) {
        this.owner = b.owner;
        this.portions = b.portions;
        this.productName = b.productName;
        this.portionsLeft = b.portionsLeft;
        this.creditValue = b.creditValue;
        this.expirationDate = b.expirationDate;
        this.fridge = b.fridge;
    }
}