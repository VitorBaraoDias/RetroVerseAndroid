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
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Adapters.ImageSliderAdapter;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Activities.ArtigoMarketPlaceDetailsActivity;
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MeuArtigoDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerViewRelatedItensLoja;
    ListaArtigosAdapter listaArtigosAdapter;
    Artigo artigo;

    TextView txtDetailsNomeLoja, txdDetailsMarcaLoja, txtPrecoDetailsLoja, txtCondicaoDetailsLoja, txtDetailsSizeLoja, txtDetailsDescricaoLoja;

    ViewPager2 viewPagerMeuArtigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meu_artigo_details);

        txtDetailsNomeLoja = findViewById(R.id.txtTituloMeuArtigo);
        txdDetailsMarcaLoja = findViewById(R.id.txdDetailsMarcaMeuArtigo);
        txtPrecoDetailsLoja = findViewById(R.id.txtPrecoDetailsMeuArtigo);
        txtCondicaoDetailsLoja = findViewById(R.id.txtCondicaoDetailsMeuArtigo);
        txtDetailsSizeLoja = findViewById(R.id.txtDetailsSizeMeuArtigo);
        txtDetailsDescricaoLoja = findViewById(R.id.txtDetailsDescricaoMeuArtigo);
        viewPagerMeuArtigo = findViewById(R.id.viewPagerMeuArtigo);




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

        List<String> images = artigo.getFotos();
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, images, artigo.getBaseUrlFoto());
        viewPagerMeuArtigo.setAdapter(adapter);
    }

    public void finishaActivity(View view) {
        finish();
    }
}