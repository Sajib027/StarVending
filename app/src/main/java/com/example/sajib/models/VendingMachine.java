package com.example.sajib.models;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.List;

public class VendingMachine {

    private static final int COIN_NICKEL = 5;
    private static final int COIN_DIME = 10;
    private static final int COIN_QUARTER = 25;

    private static final String FORMAT = "$%3.2f";
    private static final String MSG_INSERT_COIN = "Insert Coins";
    private static final String MSG_STATIC_EXACT_CHANGE_ONLY = "EXACT CHANGE ONLY";
    private static final String MSG_FORMAT_PRICE = "PRICE $%3.2f";
    private static final String MSG_NORMAL_SOLD_OUT = "SOLD OUT";
    private static final String MSG_THANK_YOU = "Collect your product, thank you";

    private final List<Stock> availableStock;

    private int currencyInUsc = 0;
    private int returnInUsc = 0;

    private int changeInUsc = 400;

    private String lastMessage = MSG_INSERT_COIN;
    public VendingMachine(@NonNull List<Stock> availableStock) {
        this.availableStock = availableStock;
        this.updateAndGetCurrentMessageForDisplay();
    }

    @Override
    public String toString() {
        return String.format("$%3.2f in flight, $%3.2f in change, and %d products",
                (float) currencyInUsc / 100,
                (float) changeInUsc / 100,
                availableStock.size());
    }

    public boolean insertCoin(int usc) {
        switch (usc) {
            case COIN_NICKEL:
            case COIN_DIME:
            case COIN_QUARTER:
                this.currencyInUsc += usc;
                this.lastMessage = String.format( FORMAT, (float) this.currencyInUsc / 100);
                return true;
            default:
                this.returnInUsc += usc;
                return false;
        }
    }

    @NonNull
    public String updateAndGetCurrentMessageForDisplay() {
        String msgToDeliver = this.lastMessage;
        if (this.currencyInUsc == 0) {
            this.lastMessage = MSG_INSERT_COIN;
        } else {
            this.lastMessage = String.format(FORMAT,
                    (float) this.currencyInUsc / 100);
        }
        return msgToDeliver;
    }

    public int getAcceptedUsc() {
        return currencyInUsc;
    }

    public int getUscInReturn() {
        return returnInUsc;
    }

    public boolean purchaseProduct(int productIndex) {
        return productIndex < availableStock.size() &&
                tryToPurchase(availableStock.get(productIndex));
    }

    private boolean tryToPurchase(@NonNull final Stock stock) {
        if (stock.getAvailable() == 0) {
            this.lastMessage = MSG_NORMAL_SOLD_OUT;
            return false;
        }
        Product product = stock.getProduct();

        if (this.currencyInUsc - product.getCostInUsc() < 0) {
            this.lastMessage = String.format(MSG_FORMAT_PRICE,
                    (float) product.getCostInUsc() / 100);
            return false;
        } else {
            stock.reduceAvailable();
            this.currencyInUsc -= product.getCostInUsc();
            this.changeInUsc -= this.currencyInUsc;
            this.returnInUsc += this.currencyInUsc;
            this.currencyInUsc = 0;
            this.lastMessage = MSG_THANK_YOU;
            return true;
        }
    }

    public void returnCoins() {
        this.returnInUsc += this.currencyInUsc;
        this.currencyInUsc = 0;
        this.updateAndGetCurrentMessageForDisplay();
    }

    public void collectCoins() {
        this.returnInUsc = 0;
    }

    public List<Product> getProducts() {
        return Stream.of(this.availableStock).map(Stock::getProduct).toList();
    }
}
