package com.example.sajib.viewmodels;

import java.util.ArrayList;
import java.util.List;

import com.example.sajib.models.Product;
import com.example.sajib.models.Stock;
import com.example.sajib.models.VendingMachine;

public final class VendingMachineRepository {
    private static VendingMachineRepository instance = null;
    private static VendingMachine vendingMachine = null;

    private VendingMachineRepository() {
        final List<Stock> stock = new ArrayList<>();
        stock.add(new Stock(new Product("Coke", 50), 2));
        stock.add(new Stock(new Product("Chips", 25), 3));
        stock.add(new Stock(new Product("Cokies", 10), 50));
        stock.add(new Stock(new Product("Chocolate", 65), 50));
        vendingMachine = new VendingMachine(stock);
    }

    public static VendingMachineRepository getInstance() {
        if (instance == null) {
            instance = new VendingMachineRepository();
        }
        return instance;
    }

    VendingMachine getVendingMachine() {
        return vendingMachine;
    }
}
