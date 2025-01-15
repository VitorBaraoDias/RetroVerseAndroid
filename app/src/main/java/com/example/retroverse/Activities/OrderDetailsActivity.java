package com.example.retroverse.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.CheckoutAdapter;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Venda;
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


        Venda venda = (Venda) getIntent().getSerializableExtra("venda");
        setAdapter(venda.getLinhasVenda());
        carregarOrderDetalhes(venda);

        Toast.makeText(this, String.valueOf(venda.getLinhasVenda().size()), Toast.LENGTH_SHORT).show();
    }
    private void setAdapter(ArrayList<Artigo> artigos) {
        if (listaArtigosAdapter == null) {

            listaArtigosAdapter = new ListaArtigosAdapter(artigos, this);


            ///normal
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(listaArtigosAdapter);

            ///normal
        } else {
            listaArtigosAdapter.notifyDataSetChanged();
        }
    }

    private void carregarOrderDetalhes(Venda venda){
        txtCodeOrderDetails.setText("ORDER #"+ venda.getCodigo());
        txtDataOrderDetails.setText(venda.getDataVenda());
        txtUsanameOrderDetails.setText(venda.getNome());
        txtLocationOrderDetails.setText(venda.getMorada());
        txtPostalCodeCountryCityOrderDetails.setText(venda.getCodigo() + ", " + venda.getPais() + ", " + venda.getCidade());
        txtMetodoExpedicaoOrderDetails.setText(venda.getMetodoExpedicao());
        txtMetodoPagamentoOrderDetails.setText(venda.getTipoPagamento());
        txtPriceTotalOrderDetails.setText( String.valueOf(venda.getTotal()));
    }

    public void finishActivity(View view) {
        finish();
    }
}