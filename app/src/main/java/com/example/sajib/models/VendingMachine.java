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
    private static final String MSG_PRICE_CHART = "Coke 50 cents, Chips 25 cents, Cokies 10 cents, Chocolate 65 cents";
    private static final String MSG_NORMAL_SOLD_OUT = "SOLD OUT";
    private static final String MSG_NORMAL_INSUFICIENT_BALANCE = "Price $%3.2f, your balance is insufficient";
    private static final String MSG_THANK_YOU = "Collect your product, thank you";

    private final List<Stock> availableStock;

    private int currencyInUsc = 0;
    private int returnInUsc = 0;
    private int changeInUsc = 0;

    private String lastMessage = MSG_INSERT_COIN;

    public VendingMachine(@NonNull List<Stock> availableStock) {
        this.availableStock = availableStock;
        updateAndGetCurrentMessageForDisplay();
    }

    public boolean insertCoin(int usc) {
        switch (usc) {
            case COIN_NICKEL:
            case COIN_DIME:
            case COIN_QUARTER:
                currencyInUsc += usc;
                lastMessage = String.format(FORMAT, (float) this.currencyInUsc / 100);
                return true;
            default:
                returnInUsc += usc;
                return false;
        }
    }

    public String updateAndGetCurrentMessageForDisplay() {
        String msgToDeliver = this.lastMessage;
        return msgToDeliver;
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
            lastMessage = MSG_NORMAL_SOLD_OUT;
            return false;
        }
        Product product = stock.getProduct();

        if (currencyInUsc - product.getCostInUsc() < 0) {
            lastMessage = MSG_NORMAL_INSUFICIENT_BALANCE;
            return false;
        } else {
            stock.reduceAvailable();
            currencyInUsc -= product.getCostInUsc();
            changeInUsc -= currencyInUsc;
            returnInUsc += currencyInUsc;
            currencyInUsc = 0;
            lastMessage = MSG_THANK_YOU;
            return true;
        }
    }

    public void priceChart() {
        lastMessage = MSG_PRICE_CHART;
    }

    public void returnCoins() {
        returnInUsc += this.currencyInUsc;
        currencyInUsc = 0;
        updateAndGetCurrentMessageForDisplay();
    }

    public void collectCoins() {
        returnInUsc = 0;
    }

    public List<Product> getProducts() {
        return Stream.of(this.availableStock).map(Stock::getProduct).toList();
    }
}
