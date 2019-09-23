package com.example.sajib.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import com.example.sajib.R;
import com.example.sajib.models.Product;
import com.example.sajib.models.VendingMachine;

public final class VendingMachineViewModel extends AndroidViewModel {
    private final MutableLiveData<String> display = new MutableLiveData<>();
    private final MutableLiveData<String> chart = new MutableLiveData<>();
    private final MutableLiveData<String> change = new MutableLiveData<>();
    private final MutableLiveData<String> product1 = new MutableLiveData<>();
    private final MutableLiveData<String> product2 = new MutableLiveData<>();
    private final MutableLiveData<String> product3 = new MutableLiveData<>();
    private final MutableLiveData<String> product4 = new MutableLiveData<>();

    private VendingMachine vendingMachine;

    public VendingMachineViewModel(Application application) {
        super(application);
    }

    public void init(VendingMachineRepository vendingMachineRepository) {
        if (vendingMachine == null) {
            vendingMachine = vendingMachineRepository.getVendingMachine();

            // initialize live data
            List<Product> products = vendingMachine.getProducts();
            product1.setValue(products.get(0).getName());
            product2.setValue(products.get(1).getName());
            product3.setValue(products.get(2).getName());
            product4.setValue(products.get(3).getName());

            updateDisplay();
        }
    }

    public LiveData<String> getVendingMachineDisplay() {
        return display;
    }

    public LiveData<String> getVendingMachineChangeDisplay() {
        return change;
    }

    public LiveData<String> getVendingMachinePriceDisplay() {
        return chart;
    }

    public LiveData<String> getVendingMachineProductDisplay(int productIndex) {
        switch (productIndex) {
            case 0:
                return product1;
            case 1:
                return product2;
            case 2:
                return product3;
            case 3:
                return product4;
            default:
                throw new IllegalArgumentException(
                        "Only 4 products available but item " + (productIndex - 1) + " was requested");
        }
    }

    public void collectCoins() {
        if (vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }
        vendingMachine.collectCoins();
        updateDisplay();
    }

    public boolean insertCoin(int coinValue) {
        if (vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        final boolean result = vendingMachine.insertCoin(coinValue);
        updateDisplay();
        return result;
    }

    public void purchaseProduct(int productIndex) {
        if (vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        vendingMachine.purchaseProduct(productIndex);
        updateDisplay();
    }

    public void priceChart() {
        if (vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }
        vendingMachine.priceChart();
        updateDisplay();
    }

    public void returnCoins() {
        if (vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        vendingMachine.returnCoins();
        updateDisplay();
    }

    private void updateDisplay() {
        if (vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        display.setValue(vendingMachine.updateAndGetCurrentMessageForDisplay());
        change.setValue(getApplication().getResources().getString(R.string.vend_action_collect,
                (float) vendingMachine.getUscInReturn() / 100));
    }
}
