package com.example.retroverse.Activities;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retroverse.Adapters.ListViewOrdersAdapter;
import com.example.retroverse.Models.Fatura;
import com.example.retroverse.R;
import com.example.retroverse.Utils.PdfUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DetalheFaturaActivity extends AppCompatActivity {
    ListView listView;
    TextView txtDateFaturaDetails, txtUsernameFaturaDetails, txtTotalFaturaDetails,
            txtLocalidadeFaturaDetails, txtCodeCityFaturaDetails, txtEmailFaturaDetails;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhe_fatura);
        listView = findViewById(R.id.listViewOrderDetails);
        txtDateFaturaDetails = findViewById(R.id.txtDateFaturaDetails);
        txtUsernameFaturaDetails = findViewById(R.id.txtUsernameFaturaDetails);
        txtLocalidadeFaturaDetails = findViewById(R.id.txtLocalidadeFaturaDetails);
        txtCodeCityFaturaDetails = findViewById(R.id.txtCodeCityFaturaDetails);
        txtEmailFaturaDetails = findViewById(R.id.txtEmailFaturaDetails);
        txtTotalFaturaDetails = findViewById(R.id.txtTotalFaturaDetails);
        floatingActionButton = findViewById(R.id.floatingActionButton);


        Fatura fatura = (Fatura) getIntent().getSerializableExtra("venda");
        listView.setAdapter(new ListViewOrdersAdapter(this,fatura));

        carregarDadosFatura(fatura);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = findViewById(android.R.id.content);

                floatingActionButton.setVisibility(View.INVISIBLE);
                PdfUtils.createPdfFromView(rootView, "invoice-" + fatura.getCodigo());
                Snackbar.make(rootView, "invoice saved", Snackbar.LENGTH_SHORT).show();
                floatingActionButton.setVisibility(View.VISIBLE);

            }
        });
    }
    private void carregarDadosFatura(Fatura fatura){
        txtDateFaturaDetails.setText(fatura.getDataVenda());
        txtUsernameFaturaDetails.setText(fatura.getNome());
        txtLocalidadeFaturaDetails.setText(fatura.getMorada());
        txtCodeCityFaturaDetails.setText(fatura.getCodigopostal() + ", " + fatura.getCidade());
        txtEmailFaturaDetails.setText(fatura.getEmail());
        txtTotalFaturaDetails.setText(fatura.getPrecoFormatado());

    }
}