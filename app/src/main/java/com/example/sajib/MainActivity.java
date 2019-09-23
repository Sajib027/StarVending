package com.example.sajib;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajib.viewmodels.VendingMachineRepository;
import com.example.sajib.viewmodels.VendingMachineViewModel;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, AlertDialog.OnClickListener {
    private AlertDialog insertCoins;

    private EditText insertCoinsInput;

    private VendingMachineViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(VendingMachineViewModel.class);
        viewModel.init(VendingMachineRepository.getInstance());

        viewModel.getVendingMachineDisplay().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String vendingDisplay) {
                ((TextView) findViewById(R.id.vend_display)).setText(vendingDisplay);
            }
        });

        viewModel.getVendingMachineChangeDisplay().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String vendingChangeDisplay) {
                ((TextView) findViewById(R.id.vend_btn_collect)).setText(vendingChangeDisplay);
            }
        });

        final TextView product1 = findViewById(R.id.vend_btn_purchase_1);
        final TextView product2 = findViewById(R.id.vend_btn_purchase_2);
        final TextView product3 = findViewById(R.id.vend_btn_purchase_3);
        final TextView product4 = findViewById(R.id.vend_btn_purchase_4);
        viewModel.getVendingMachineProductDisplay(0).observe(this, product1::setText);
        viewModel.getVendingMachineProductDisplay(1).observe(this, product2::setText);
        viewModel.getVendingMachineProductDisplay(2).observe(this, product3::setText);
        viewModel.getVendingMachineProductDisplay(3).observe(this, product4::setText);

        // handlers
        findViewById(R.id.vend_btn_chart).setOnClickListener(this);
        findViewById(R.id.vend_btn_return).setOnClickListener(this);
        findViewById(R.id.vend_btn_insert).setOnClickListener(this);
        product1.setOnClickListener(this);
        product2.setOnClickListener(this);
        product3.setOnClickListener(this);
        product4.setOnClickListener(this);
        findViewById(R.id.vend_btn_collect).setOnClickListener(this);

        insertCoinsInput = new EditText(this);
        insertCoinsInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        insertCoins = new AlertDialog.Builder(this)
                .setTitle(R.string.vend_dlg_insert_coin)
                .setMessage(R.string.vend_coin)
                .setView(insertCoinsInput)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
                .create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vend_btn_chart:
                viewModel.priceChart();
                break;
            case R.id.vend_btn_collect:
                viewModel.collectCoins();
                break;
            case R.id.vend_btn_insert:
                insertCoins.show();
                return;
            case R.id.vend_btn_purchase_1:
                viewModel.purchaseProduct(0);
                break;
            case R.id.vend_btn_purchase_2:
                viewModel.purchaseProduct(1);
                break;
            case R.id.vend_btn_purchase_3:
                viewModel.purchaseProduct(2);
                break;
            case R.id.vend_btn_purchase_4:
                viewModel.purchaseProduct(3);
                break;
            case R.id.vend_btn_return:
                viewModel.returnCoins();
                break;
        }
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        try {
            float usdValue = Float.parseFloat(insertCoinsInput.getText().toString());
            int coinValue = (int) Math.floor(usdValue * 100);

            if (viewModel.insertCoin(coinValue)) {
                insertCoinsInput.setText("");
            } else {
                Toast.makeText(this, R.string.vend_dlg_invalid, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException exc) {
            Toast.makeText(
                    this, R.string.vend_dlg_invalid, Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
