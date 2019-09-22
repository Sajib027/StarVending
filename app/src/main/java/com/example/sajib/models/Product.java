package com.example.sajib.models;

public class Product {
    private final String name;
    private final int costInUsc;

    public Product(String name, int costInUsc) {
        this.name = name;
        this.costInUsc = costInUsc;
    }

    public String getName() {
        return name;
    }

    public int getCostInUsc() {
        return costInUsc;
    }
}
