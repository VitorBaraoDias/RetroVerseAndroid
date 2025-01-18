package com.example.retroverse.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Activities.ArtigoMarketPlaceDetailsActivity;
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;


public class MeuArtigoDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerViewRelatedItensLoja;
    ListaArtigosAdapter listaArtigosAdapter;
    Artigo artigo;

    ImageView imgPrimeiraImagemDetalhesLoja;
    TextView txtDetailsNomeLoja, txdDetailsMarcaLoja, txtPrecoDetailsLoja, txtCondicaoDetailsLoja, txtDetailsSizeLoja, txtDetailsDescricaoLoja;
    public static final int ADD = 100, EDIT = 200, DELETE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meu_artigo_details);

        txtDetailsNomeLoja = findViewById(R.id.txtDetailsNomeLoja);
        txdDetailsMarcaLoja = findViewById(R.id.txdDetailsMarcaLoja);
        txtPrecoDetailsLoja = findViewById(R.id.txtPrecoDetailsLoja);
        txtCondicaoDetailsLoja = findViewById(R.id.txtCondicaoDetailsLoja);
        txtDetailsSizeLoja = findViewById(R.id.txtDetailsSizeLoja);
        txtDetailsDescricaoLoja = findViewById(R.id.txtDetailsDescricaoLoja);
        imgPrimeiraImagemDetalhesLoja = findViewById(R.id.imgPrimeiraImagemDetalhesLoja);



        int id = getIntent().getIntExtra("ID", 0);
        artigo = Singleton.getInstance(this).getArtigo(id);


        carregarArtigo();
    }

    private void carregarArtigo(){
        txtDetailsNomeLoja.setText(artigo.getNome());
        txtPrecoDetailsLoja.setText(artigo.getPrecoFormatado());
        txtCondicaoDetailsLoja.setText(artigo.getEstado());
        txdDetailsMarcaLoja.setText(artigo.getMarca());
        txtDetailsSizeLoja.setText(artigo.getTamanho());
        txtDetailsDescricaoLoja.setText(artigo.getDescricao());

        Glide.with(this)
                .load(artigo.getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPrimeiraImagemDetalhesLoja);
    }
}