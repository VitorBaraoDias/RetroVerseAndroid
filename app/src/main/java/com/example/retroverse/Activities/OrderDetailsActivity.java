package com.example.retroverse.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Fatura;
import com.example.retroverse.R;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView txtDataOrderDetails, txtUsanameOrderDetails, txtLocationOrderDetails,
            txtPostalCodeCountryCityOrderDetails, txtPriceTotalOrderDetails, txtMetodoExpedicaoOrderDetails,
            txtMetodoPagamentoOrderDetails, txtCodeOrderDetails;
    RecyclerView recyclerView;
    private ListaArtigosAdapter listaArtigosAdapter;

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
        setAdapter(fatura.getArtigos());
        carregarOrderDetalhes(fatura);

    }
    private void setAdapter(ArrayList<Artigo> artigos) {
        if (listaArtigosAdapter == null) {

            listaArtigosAdapter = new ListaArtigosAdapter(artigos, this, false);


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
}