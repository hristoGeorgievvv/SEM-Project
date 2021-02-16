package nl.tudelft.sem.sem54.fridge.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.Objects;

@Table(name = "ProductTransaction")
@Entity
public class ProductTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductTransaction that = (ProductTransaction) o;
        return credits == that.credits
                && portions == that.portions
                && isRevert == that.isRevert
                && Objects.equals(user, that.user)
                && Objects.equals(product, that.product)
                && Objects.equals(revertedProductTransaction, that.revertedProductTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, user, product, credits, portions, isRevert, revertedProductTransaction);
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false)
    private int portions;

    @Column(nullable = false)
    private boolean isRevert;

    @OneToOne
    @JoinColumn(nullable = true)
    private ProductTransaction revertedProductTransaction;

    /**
     * Create a productTransaction (currently only used for testing).
     */
    public ProductTransaction(Timestamp timestamp, User user, Product product,
                              int credits, int portions, boolean isRevert,
                              ProductTransaction revertedProductTransaction) {
        this.timestamp = timestamp;
        this.user = user;
        this.product = product;
        this.credits = credits;
        this.portions = portions;
        this.isRevert = isRevert;
        this.revertedProductTransaction = revertedProductTransaction;
    }

    /**
     * Create a productTransaction (currently only used for testing).
     */
    public ProductTransaction(Timestamp timestamp, User user, Product product,
                              int credits, int portions, boolean isRevert
    ) {
        this.timestamp = timestamp;
        this.user = user;
        this.product = product;
        this.credits = credits;
        this.portions = portions;
        this.isRevert = isRevert;
    }

    /**
     * Constructor for testing.
     *
     * @param product  the product for the transaction
     * @param portions the number of portions of the transaction
     */
    public ProductTransaction(Product product, int portions) {
        this.product = product;
        this.portions = portions;
    }

    /**
     * Empty constructor for testing.
     */
    public ProductTransaction() {
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public boolean getIsRevert() {
        return isRevert;
    }

    public void setRevert(boolean revert) {
        isRevert = revert;
    }


    public ProductTransaction getRevertedProductTransaction() {
        return revertedProductTransaction;
    }

    public void setRevertedProductTransaction(ProductTransaction revertedProductTransaction) {
        this.revertedProductTransaction = revertedProductTransaction;
    }


    public static class Builder {
        private Timestamp timestamp;
        private User user;
        private Product product;
        private int credits;
        private int portions;
        private boolean isRevert;
        private ProductTransaction revertedProductTransaction;

        public Builder setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setCredits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder setPortions(int portions) {
            this.portions = portions;
            return this;
        }

        public Builder setRevert(boolean isRevert) {
            this.isRevert = isRevert;
            return this;
        }

        public Builder setProductTransaction(ProductTransaction revertedProductTransaction) {
            this.revertedProductTransaction = revertedProductTransaction;
            return this;
        }

        public ProductTransaction build() {
            return new ProductTransaction(this);
        }
    }

    private ProductTransaction(Builder b) {
        this.timestamp = b.timestamp;
        this.user = b.user;
        this.product = b.product;
        this.credits = b.credits;
        this.portions = b.portions;
        this.isRevert = b.isRevert;
        this.revertedProductTransaction = b.revertedProductTransaction;
    }
}

