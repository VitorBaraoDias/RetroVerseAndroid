package com.example.retroverse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Singleton.Singleton;

public class ArtigoMarketPlaceDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerViewRelatedItenS;
    ListaArtigosAdapter listaArtigosAdapter;
    Artigo artigo;

    ImageView imgPrimeiraImagemDetalhesMarketplace, imgPerfilMarketplace;
    TextView txtDetailsNomeMarketPlace, txdDetailsMarcaMarketplace, txtPrecoDetailsMarketplace, txtCondicaoDetailsMarketplace, txtDetailsSizeMarketplace, txtDetailsDescricaoMarketplace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_artigo_market_place_details);


        recyclerViewRelatedItenS = findViewById(R.id.recyclerViewRelatedItensMarketplace);
        txtDetailsNomeMarketPlace = findViewById(R.id.txtDetailsNomeMarketplace);
        txdDetailsMarcaMarketplace = findViewById(R.id.txdDetailsMarcaMarketplace);
        txtPrecoDetailsMarketplace = findViewById(R.id.txtPrecoDetailsMarketplace);
        txtCondicaoDetailsMarketplace = findViewById(R.id.txtCondicaoDetailsMarketplace);
        txtDetailsSizeMarketplace = findViewById(R.id.txtDetailsSizeMarketplace);
        txtDetailsDescricaoMarketplace = findViewById(R.id.txtDetailsDescricaoMartkeplace);
        imgPrimeiraImagemDetalhesMarketplace = findViewById(R.id.imgPrimeiraImagemDetalhesMarketplace);
        imgPerfilMarketplace = findViewById(R.id.imgPerfilMarketplace);


        int id = getIntent().getIntExtra("ID", 0);
        artigo = Singleton.getInstance(this).getArtigo(id);

        carregarArtigo();

    }
    private void carregarArtigo(){
        //txtDetailsNomeMarketPlace.setText(artigo.getNome());
        txtPrecoDetailsMarketplace.setText(artigo.getPrecoFormatado());
        txtCondicaoDetailsMarketplace.setText(artigo.getEstado());
        txdDetailsMarcaMarketplace.setText(artigo.getMarca());
        txtDetailsSizeMarketplace.setText(artigo.getTamanho());
        txtDetailsDescricaoMarketplace.setText(artigo.getDescricao());

        Glide.with(this)
                .load(artigo.getPerfil().getFotoperfil())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPerfilMarketplace);
        Glide.with(this)
                .load(artigo.getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPrimeiraImagemDetalhesMarketplace);
    }

    public void addToCart(View view) {
    }
}