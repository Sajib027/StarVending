package com.example.sajib.models;

public class Stock {
    private final Product product;
    private int available;

    public Stock(Product product, int available) {
        this.product = product;
        this.available = available;
    }

    public Product getProduct() {
        return product;
    }

    public int getAvailable() {
        return available;
    }

    public void reduceAvailable() {
        if (this.available < 1) {
            throw new UnsupportedOperationException(
                    String.format("No stock for %s is available at this time.",
                            this.product));
        }
        this.available--;
    }
}
