package com.example.sajib.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ProductTest {
    @Test
    public void testProduct() throws Exception {
        Product propduct = new Product("Coke", 50);
        assertEquals("Coke", propduct.getName());
        assertEquals(50, propduct.getCostInUsc());
    }
}