package com.example.retroverse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.HistoricoAdapter;
import com.example.retroverse.Listeners.RefreshFaturasListener;
import com.example.retroverse.Models.Fatura;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;

public class HistoricoActivity extends AppCompatActivity implements RefreshFaturasListener, HistoricoAdapter.OnItemClickListener, HistoricoAdapter.OnItemClickOpenInvoiceListener {


    Button buttonGetAllOrders, btnGetAllOrdersShipped, btnGetAllOrdersCompleted;
    RecyclerView recyclerViewOrders;
    HistoricoAdapter historicoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historico);

        buttonGetAllOrders = findViewById(R.id.buttonGetAllOrders);
        btnGetAllOrdersShipped = findViewById(R.id.btnGetAllOrdersShipped);
        btnGetAllOrdersCompleted = findViewById(R.id.btnGetAllOrdersCompleted);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);


        Singleton.getInstance(this).setRefreshFaturasListener(this);
        Singleton.getInstance(this).getAllFaturasAPI(Utils.getToken(this), this);

    }

    public void backMenuActivity(View view) {
        finish();
    }
    private void setAdapter(ArrayList<Fatura> faturas) {
        if (historicoAdapter == null) {
            // Inicializa os adaptadores com as listas separadas

            historicoAdapter = new HistoricoAdapter(faturas, getApplicationContext());
            historicoAdapter.setOnItemClickListener(this);
            historicoAdapter.setOnItemInvoiceClickListener(this);

            ///normal
            recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerViewOrders.setItemAnimator(new DefaultItemAnimator());
            recyclerViewOrders.setAdapter(historicoAdapter);

        } else {
            historicoAdapter.updateData(faturas);
            historicoAdapter.notifyDataSetChanged();
        }
    }
    public void onButtonClick(View view) {
        // Resetar os estilos de todos os botões
        resetButtonStyles();

        // Alterar o estilo do botão clicado
        Button clickedButton = (Button) view; // Cast para Button
        clickedButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.black));



        if (view.getId() == R.id.buttonGetAllOrders) {
            setAdapter(Singleton.getInstance(this).getFaturas());

        } else if (view.getId() == R.id.btnGetAllOrdersShipped) {
            setAdapter(Singleton.getInstance(this).getFaturasIncompletas());
        } else if (view.getId() == R.id.btnGetAllOrdersCompleted) {
            setAdapter(Singleton.getInstance(this).getFaturasEntregasCompletas());
        }
    }
    private void resetButtonStyles() {
        buttonGetAllOrders.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        btnGetAllOrdersShipped.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        btnGetAllOrdersCompleted.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        buttonGetAllOrders.setTextColor(ContextCompat.getColor(this, R.color.black));
        btnGetAllOrdersShipped.setTextColor(ContextCompat.getColor(this, R.color.black));
        btnGetAllOrdersCompleted.setTextColor(ContextCompat.getColor(this, R.color.black));

    }

    @Override
    public void onRefreshFaturas(ArrayList<Fatura> faturas) {
        setAdapter(faturas);
    }

    @Override
    public void onItemClick(Fatura fatura, int position) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("venda", fatura);
        startActivity(intent);
    }

    @Override
    public void onOpenInvoiceDetailsClick(Fatura fatura, int position) {
        Intent intent = new Intent(this, DetalheFaturaActivity.class);
        intent.putExtra("venda", fatura);
        startActivity(intent);
    }
}