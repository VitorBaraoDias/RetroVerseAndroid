package com.example.retroverse.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Adapters.ListaOrderDetailsAdapter;
import com.example.retroverse.Listeners.RefreshStateListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Fatura;
import com.example.retroverse.Models.Linhavenda;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity implements ListaOrderDetailsAdapter.OnItemClickListener, RefreshStateListener {

    TextView txtDataOrderDetails, txtUsanameOrderDetails, txtLocationOrderDetails,
            txtPostalCodeCountryCityOrderDetails, txtPriceTotalOrderDetails, txtMetodoExpedicaoOrderDetails,
            txtMetodoPagamentoOrderDetails, txtCodeOrderDetails;
    RecyclerView recyclerView;
    private ListaOrderDetailsAdapter listaArtigosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);

        txtDataOrderDetails = findViewById(R.id.txtDataOrderDetails);
        txtUsanameOrderDetails = findViewById(R.id.txtUsanameOrderDetails);
        txtLocationOrderDetails = findViewById(R.id.txtLocationOrderDetails);
        txtPostalCodeCountryCityOrderDetails = findViewById(R.id.txtPostalCodeCountryCityOrderDetails);
        txtPriceTotalOrderDetails = findViewById(R.id.txtPriceTotalOrderDetails);
        txtMetodoExpedicaoOrderDetails = findViewById(R.id.txtMetodoExpedicaoOrderDetails);
        txtMetodoPagamentoOrderDetails = findViewById(R.id.txtMetodoPagamentoOrderDetails);
        txtCodeOrderDetails = findViewById(R.id.txtCodeOrderDetails);
        recyclerView = findViewById(R.id.recyclerView);


        Fatura fatura = (Fatura) getIntent().getSerializableExtra("venda");
        setAdapter(fatura.getLinhasVenda());
        carregarOrderDetalhes(fatura);


        Singleton.getInstance(this).setRefreshStateListener(this);
    }
    private void setAdapter(ArrayList<Linhavenda> linhavendas) {
        if (listaArtigosAdapter == null) {

            listaArtigosAdapter = new ListaOrderDetailsAdapter(linhavendas, this, false);
            listaArtigosAdapter.setOnItemClickListener(this);

            ///normal
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(listaArtigosAdapter);

            ///normal
        } else {
            listaArtigosAdapter.notifyDataSetChanged();
        }
    }

    private void carregarOrderDetalhes(Fatura fatura){
        txtCodeOrderDetails.setText("ORDER #"+ fatura.getCodigo());
        txtDataOrderDetails.setText(fatura.getDataVenda());
        txtUsanameOrderDetails.setText(fatura.getNome());
        txtLocationOrderDetails.setText(fatura.getMorada());
        txtPostalCodeCountryCityOrderDetails.setText(fatura.getCodigo() + ", " + fatura.getPais() + ", " + fatura.getCidade());
        txtMetodoExpedicaoOrderDetails.setText(fatura.getMetodoExpedicao());
        txtMetodoPagamentoOrderDetails.setText(fatura.getTipoPagamento());
        txtPriceTotalOrderDetails.setText( String.valueOf(fatura.getTotal()));
    }

    public void finishActivity(View view) {
        finish();
    }

    @Override
    public void onItemClick(Linhavenda linhavenda, int position, TextView txt, Button btn) {
        Singleton.getInstance(this).putStateOrderAPI(Utils.getToken(this), linhavenda.getId(),  this);
        txt.setText("COMPLETED");
        btn.setVisibility(View.GONE);
    }

    @Override
    public void onRefreshState() {
        Singleton.getInstance(this).getAllFaturasAPI(Utils.getToken(this), this);
    }
}