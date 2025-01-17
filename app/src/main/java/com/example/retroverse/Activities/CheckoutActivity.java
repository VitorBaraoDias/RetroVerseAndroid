package com.example.retroverse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.CheckoutAdapter;
import com.example.retroverse.Fragments.ShippingAddresFragment;
import com.example.retroverse.Listeners.CheckoutListener;
import com.example.retroverse.Listeners.MetodoExpedicaoListener;
import com.example.retroverse.Listeners.TipoPagamentoListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.Models.Metodoexpedicao;
import com.example.retroverse.Models.Tipopagamento;
import com.example.retroverse.Models.Venda;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity implements ShippingAddresFragment.OnShippingDetailsListener, TipoPagamentoListener, MetodoExpedicaoListener, CheckoutListener {

    Carrinho carrinho;
    private CheckoutAdapter checkoutAdapter;
    TextView txtUsanameCheckout, txtLocationCheckout, txtPostalCodeCheckout, txtPrecoItemsCheckout, txtPrecoTotalItemsCheckout;

    String name, country, city, addres, postalCode;
    int selectedIdTipoPagamento = 0, selectedIdMetodoExpedicao = 0;
    TextInputLayout textInputLayoutMetodoPagamento, textInputLayoutMetodoExpedicao;
    RecyclerView recyclerView;
    AutoCompleteTextView dropdownMenuTipoPagemento, dropdownMenuMetodoExpedicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        recyclerView = findViewById(R.id.recyclerCheckout);
        txtUsanameCheckout = findViewById(R.id.txtUsanameCheckout);
        txtLocationCheckout = findViewById(R.id.txtLocationCheckout);
        txtPostalCodeCheckout = findViewById(R.id.txtPostalCodeCountryCityCheckout);
        textInputLayoutMetodoPagamento = findViewById(R.id.textInputLayoutMetodoPagamento);
        textInputLayoutMetodoExpedicao = findViewById(R.id.textInputLayoutMetodoExpedicao);
        txtPrecoItemsCheckout = findViewById(R.id.txtPrecoItemsCheckout);
        txtPrecoTotalItemsCheckout = findViewById(R.id.txtPrecoTotalItemsCheckout);
        dropdownMenuTipoPagemento = findViewById(R.id.dropdown_menu);
        dropdownMenuMetodoExpedicao = findViewById(R.id.dropdown_menuMetodoExpedicao);



        Singleton.getInstance(this).setTipoPagamentoListener(this);
        Singleton.getInstance(this).setMetodoexpedicaoListener(this);
        Singleton.getInstance(this).setCheckoutListener(this);


        Singleton.getInstance(this).getAllATiposPagamentoAPI(Utils.getToken(this), this);
        Singleton.getInstance(this).getAllMetodoExpedicaoAPI(Utils.getToken(this), this);

        carrinho  = Singleton.getInstance(this).getCart();
        setAdapter(carrinho.getLinhasCarrinho());

        setInforOrderPrice(carrinho);



        dropdownMenuTipoPagemento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtendo o objeto associado à posição
                Tipopagamento tipopagamento = Singleton.getInstance(getApplicationContext()).getTipoPagementoByPosition(position);
                if (tipopagamento != null) {
                    // Salvando o ID selecionado
                    selectedIdTipoPagamento = tipopagamento.getId();
                }
            }
        });
        dropdownMenuMetodoExpedicao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Metodoexpedicao metodoexpedicao = Singleton.getInstance(getApplicationContext()).getMetodoExpedicaoByPosition(position);
                if (metodoexpedicao != null) {
                    selectedIdMetodoExpedicao = metodoexpedicao.getId();
                }
            }
        });
    }
    private void setAdapter(ArrayList<Artigo> artigos) {
        if (checkoutAdapter == null) {

            checkoutAdapter = new CheckoutAdapter(artigos, this);


            ///normal
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(checkoutAdapter);

            ///normal
        } else {
            checkoutAdapter.notifyDataSetChanged();
        }
    }
    private void setInforOrderPrice(Carrinho carrinho){
        txtPrecoItemsCheckout.setText(carrinho.getTotalPriceFormatted());
        txtPrecoTotalItemsCheckout.setText(carrinho.getTotalPriceFormatted());
    }

    public void openCart(View view) {
        Intent intent = new Intent(this, CarrinhoActivity.class);
        startActivity(intent);
        finish();
    }

    public void openDialogFragment(View view) {
        ShippingAddresFragment mdf = new ShippingAddresFragment();
        mdf.show(getSupportFragmentManager(), "TAG");
    }

    private boolean validatePaymentMethod() {
        String selectedPaymentMethod = textInputLayoutMetodoPagamento.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(selectedPaymentMethod)) {
            textInputLayoutMetodoPagamento.setError("Please select a payment method");
            return false;
        } else {
            textInputLayoutMetodoPagamento.setError(null);
            return true;
        }
    }
    private boolean validateShippingMethod() {

        String selectedMethodExpedicao = textInputLayoutMetodoPagamento.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(selectedMethodExpedicao)) {
            textInputLayoutMetodoPagamento.setError("Please select a shipping method");
            return false;
        } else {
            textInputLayoutMetodoPagamento.setError(null);
            return true;
        }
    }
    @Override
    public void onShippingDetailsConfirmed(String name, String country, String city, String address, String postalCode) {

        this.name = name;
        this.country = country;
        this.city = city;
        this.addres = address;
        this.postalCode = postalCode;

        txtUsanameCheckout.setText(name);
        txtLocationCheckout.setText(address);
        txtPostalCodeCheckout.setText(country + ", " + country + ", " + city);
    }

    public void finishOrder(View view) {
        if(validatePaymentMethod() && validateShippingMethod()){
            Singleton.getInstance(this).addCheckoutAPI(Utils.getToken(this),
                    selectedIdMetodoExpedicao, selectedIdTipoPagamento, name, postalCode,
                    addres, country, city, this);
        }
    }

    @Override
    public void onRefreshTipoPagamento(ArrayList<Tipopagamento> tipopagamentos) {
        ArrayAdapter<Tipopagamento> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tipopagamentos);
        dropdownMenuTipoPagemento.setAdapter(adapter);
    }
    @Override
    public void onRefreshMetodoExpedicao(ArrayList<Metodoexpedicao> metodoexpedicaos) {
        ArrayAdapter<Metodoexpedicao> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, metodoexpedicaos);
        dropdownMenuMetodoExpedicao.setAdapter(adapter);
    }

    @Override
    public void onOrderDetails(Venda venda) {

        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("venda", venda);
        startActivity(intent);
        finish();

    }
}