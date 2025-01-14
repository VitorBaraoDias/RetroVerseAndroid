package com.example.retroverse.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.CarrinhoAdapter;
import com.example.retroverse.Adapters.CheckoutAdapter;
import com.example.retroverse.Fragments.ShippingAddresFragment;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    Carrinho carrinho;
    RecyclerView recyclerView;
    private CheckoutAdapter checkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        recyclerView = findViewById(R.id.recyclerCheckout);

        carrinho  = Singleton.getInstance(this).getCart();
        setAdapter(carrinho.getLinhasCarrinho());


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

    public void openCart(View view) {
        Intent intent = new Intent(this, CarrinhoActivity.class);
        startActivity(intent);
        finish();
    }

    public void openDialogFragment(View view) {
        ShippingAddresFragment mdf = new ShippingAddresFragment();
        mdf.show(getSupportFragmentManager(), "TAG");
    }
}