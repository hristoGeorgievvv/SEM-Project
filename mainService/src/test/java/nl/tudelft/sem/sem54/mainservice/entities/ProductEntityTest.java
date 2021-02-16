package nl.tudelft.sem.sem54.mainservice.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductEntityTest {

    ProductEntity testProduct;

    private int portions1;
    private int portions2;
    private int credits1;
    private int credits2;
    private UserEntity owner1;
    private UserEntity owner2;

    @BeforeEach
    void beforeEach() {
        portions1 = new Random().nextInt(1000);
        portions2 = new Random().nextInt(1000);
        credits1 = new Random().nextInt(1000);
        credits2 = new Random().nextInt(1000);
        owner1 = new UserEntity("test1");
        owner2 = new UserEntity("test2");
        testProduct = new ProductEntity(portions1, credits1, owner1);
    }

    @Test
    void testGetPortions() {
        assertEquals(portions1, testProduct.getPortions());
    }

    @Test
    void testGetCredits() {
        assertEquals(credits1, testProduct.getCredits());
    }

    @Test
    void testGetCreditsPerPortion() {
        assertEquals((float) credits1 / (float) portions1, testProduct.getCreditsPerPortion());
    }

    @Test
    void testGetOwner() {
        assertEquals(owner1, testProduct.getOwner());
    }

    @Test
    void testSetPortions() {
        testProduct.setPortions(portions2);
        assertEquals(portions2, testProduct.getPortions());
    }

    @Test
    void testSetCredits() {
        testProduct.setCredits(credits2);
        assertEquals(credits2, testProduct.getCredits());
    }

    @Test
    void testSetOwner() {
        testProduct.setOwner(owner2);
        assertEquals(owner2, testProduct.getOwner());
    }

    @Test
    void testEquals_itself() {
        assertThat(testProduct).isEqualTo(testProduct);
    }

    @Test
    void testEquals_null() {
        assertThat(testProduct).isNotEqualTo(null);
    }

    @Test
    void testEquals_differentClass() {
        assertThat(testProduct).isNotEqualTo(new Object());
    }

    @Test
    void testEquals_differentPortions() {
        ProductEntity otherProduct = new ProductEntity(testProduct.getPortions() + 1,
            testProduct.getCredits(), testProduct.getOwner());
        assertThat(testProduct).isNotEqualTo(otherProduct);
    }

    @Test
    void testEquals_differentCredits() {
        ProductEntity otherProduct = new ProductEntity(testProduct.getPortions(),
            testProduct.getCredits() + 1, testProduct.getOwner());
        assertThat(testProduct).isNotEqualTo(otherProduct);
    }

    @Test
    void testEquals_differentOwner() {
        ProductEntity otherProduct = new ProductEntity(testProduct.getPortions(),
            testProduct.getCredits(), new UserEntity());
        assertThat(testProduct).isNotEqualTo(otherProduct);
    }

}