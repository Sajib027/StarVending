package com.example.sajib.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import com.example.sajib.R;
import com.example.sajib.models.Product;
import com.example.sajib.models.VendingMachine;

public final class VendingMachineViewModel extends AndroidViewModel {
    private final MutableLiveData<String> display = new MutableLiveData<>();
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
        if (this.vendingMachine == null) {
            this.vendingMachine = vendingMachineRepository.getVendingMachine();

            // initialize live data
            List<Product> products = this.vendingMachine.getProducts();
            product1.setValue(products.get(0).getName());
            product2.setValue(products.get(1).getName());
            product3.setValue(products.get(2).getName());
            product4.setValue(products.get(3).getName());

            updateDisplay();
        }
    }

    public LiveData<String> getVendingMachineDisplay() {
        return this.display;
    }

    public LiveData<String> getVendingMachineChangeDisplay() {
        return this.change;
    }

    public LiveData<String> getVendingMachineProductDisplay(int productIndex) {
        switch (productIndex) {
            case 0:
                return this.product1;
            case 1:
                return this.product2;
            case 2:
                return this.product3;
            case 3:
                return  this.product4;
            default:
                throw new IllegalArgumentException(
                        "Only 4 products available but item " + (productIndex - 1) + " was requested");
        }
    }

    public void collectCoins() {
        if (this.vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        this.vendingMachine.collectCoins();
        updateDisplay();
    }

    public boolean insertCoin(int coinValue) {
        if (this.vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        final boolean result = this.vendingMachine.insertCoin(coinValue);
        updateDisplay();
        return result;
    }

    public void purchaseProduct(int productIndex) {
        if (this.vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        this.vendingMachine.purchaseProduct(productIndex);
        updateDisplay();
    }

    public void returnCoins() {
        if (this.vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        this.vendingMachine.returnCoins();
        updateDisplay();
    }

    private void updateDisplay() {
        if (this.vendingMachine == null) {
            throw new UnsupportedOperationException("you must call init() before calling any other methods in this view model");
        }

        this.display.setValue(this.vendingMachine.updateAndGetCurrentMessageForDisplay());
        this.change.setValue(this.getApplication().getResources().getString(R.string.vend_action_collect,
                        (float) this.vendingMachine.getUscInReturn() / 100));
    }
}
