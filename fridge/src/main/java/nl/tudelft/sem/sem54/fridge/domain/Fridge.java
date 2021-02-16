package nl.tudelft.sem.sem54.fridge.domain;

import nl.tudelft.sem.sem54.fridge.exceptions.ProductNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fridge fridge = (Fridge) o;
        return Objects.equals(users, fridge.users)
                && Objects.equals(products, fridge.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, products);
    }

    @OneToMany(mappedBy = "fridge")
    private List<User> users;

    @OneToMany(mappedBy = "fridge")
    private List<Product> products;

    public long getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Fridge constructor takes an id, list of users and list of products.
     *
     * @param users    list of `User` objects.
     * @param products list of `Product` objects.
     */
    public Fridge(List<User> users, List<Product> products) {
        this.users = users;
        this.products = products;
    }

    public Fridge() {

    }

    public Product addProduct(Product product) {
        products.add(product);
        return product;
    }

    /**
     * Removes a product by id.
     *
     * @param product the product to remove
     * @return product.
     * @throws ProductNotFoundException if the supplied product's
     *                                  id is not in the fridge's product list.
     */
    public Product removeProduct(Product product) throws ProductNotFoundException {
        Product productToRemove = products.stream().filter(
                p -> p.getId() == product.getId()).findFirst()
                .orElseThrow(() -> new ProductNotFoundException(
                        product.toString() + " was not found in fridge!"));

        products.remove(productToRemove);
        return product;
    }

    /**
     * Makes a list of products in the fridge that are expired.
     *
     * @return List of expired Products.
     */
    public List<Product> getExpiredProducts() {
        return products.stream()
                .filter(p -> !p.getExpirationDate().after(new Date(System.currentTimeMillis())))
                .collect(Collectors.toList());
    }

    /**
     * Makes a list of products in the fridge that are fully consumed.
     *
     * @return List of fully consumed Products.
     */
    public List<Product> getEatenProducts() {
        return products.stream()
                .filter(p -> p.getPortionsLeft() <= 0)
                .collect(Collectors.toList());
    }

    /**
     * Makes a list of products in the fridge that are available to consume.
     *
     * @return List of available Products.
     */
    public List<Product> getAvailableProducts() {
        return products.stream()
                .filter(p -> p.getExpirationDate().after(new Date(System.currentTimeMillis()))
                        && p.getPortionsLeft() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Validates a given transaction T for the fridge.
     * T must refer to a product and user contained in the fridge.
     * If T is not an undo transaction, declared portions must be <= remaining portions.
     * If T is an undo transaction, then it must undo a real transaction T' for the product
     * and the portions for T and T' must cancel out,
     * and T must happen before T'.
     *
     * @param productTransaction the transaction to validate.
     * @return true if valid, false if invalid.
     */
    public boolean isTransactionValid(ProductTransaction productTransaction) {
        // TODO: return messages based on violated rule?

        Optional<Product> product = products.stream()
                .filter(p -> p.getId() == productTransaction.getProduct().getId()).findFirst();
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == productTransaction.getUser().getId()).findFirst();

        if (product.isPresent() && user.isPresent()) {
            // referred product and referred user must exist in the fridge
            if (productTransaction.getIsRevert()) {
                Optional<ProductTransaction> undoneTransaction =
                        product.get().getTransactionList().stream()
                                .filter(t ->
                                        t.getId() == productTransaction
                                                .getRevertedProductTransaction()
                                                .getId()).findFirst();
                // valid if the undo transaction refers to a real one with the opposite portions
                // and the undo transaction happens after the original transaction
                return undoneTransaction.isPresent()
                        && undoneTransaction.get().getPortions()
                        == -productTransaction.getPortions()

                        && undoneTransaction.get().getTimestamp()
                        .before(productTransaction.getTimestamp());

            } else {
                int transactionPortions = productTransaction.getPortions();
                // valid if you can actually take the amount of requested portions
                // requested portions must be > 0, otherwise the transaction should've been undo
                return (transactionPortions > 0
                        && productTransaction.getPortions()
                        <= product.get().getPortionsLeft());
            }
        }
        return false;
    }
}
