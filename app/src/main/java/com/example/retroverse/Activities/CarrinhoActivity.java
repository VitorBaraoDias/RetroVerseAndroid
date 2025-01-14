package com.example.retroverse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.CarrinhoAdapter;
import com.example.retroverse.AuthActivities.LoginActivity;
import com.example.retroverse.Listeners.CartCountRefreshListener;
import com.example.retroverse.Listeners.CartRefreshListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoAdapter.OnItemClickListener, CartRefreshListener {

    RecyclerView recyclerView;
    private CarrinhoAdapter carrinhoAdapter;
    View rootView;
    TextView txtPrecoTotalCart;
    Carrinho carrinho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrinho);

        recyclerView = findViewById(R.id.recyclerCart);
        txtPrecoTotalCart = findViewById(R.id.txtPrecoTotalCart);
        rootView = findViewById(android.R.id.content);


        Singleton.getInstance(this).setCartRefreshListener(this);

        carrinho  = Singleton.getInstance(this).getCart();

        setAdapter(carrinho.getLinhasCarrinho());
        carregarPreco();
    }
    private void carregarPreco(){
        txtPrecoTotalCart.setText(this.carrinho.getTotalPriceFormatted());
    }
    @Override
    public void onItemClick(Artigo artigo, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Remove item")
                .setMessage("Are you sure you want to remove this item from your basket?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Ação para remover o item
                    Singleton.getInstance(this).removeItemCartApi(Utils.getToken(this), artigo, this);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
    private void setAdapter(ArrayList<Artigo> artigos) {
        if (carrinhoAdapter == null) {

            carrinhoAdapter = new CarrinhoAdapter(artigos, this);

            carrinhoAdapter.setOnItemClickListener(this);

            ///normal
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(carrinhoAdapter);

            ///normal
        } else {
            carrinhoAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onRefreshCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
        Snackbar.make(rootView, "item removed", Snackbar.LENGTH_SHORT).show();
        setAdapter( this.carrinho.getLinhasCarrinho());
        carregarPreco();
    }

    public void openCheckoutActivity(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
        finish();
    }
}