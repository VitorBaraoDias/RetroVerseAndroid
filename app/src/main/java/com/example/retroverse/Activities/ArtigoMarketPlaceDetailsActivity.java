package com.example.retroverse.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils;

public class ArtigoMarketPlaceDetailsActivity extends AppCompatActivity implements CartListener {

    RecyclerView recyclerViewRelatedItenS;
    ListaArtigosAdapter listaArtigosAdapter;
    Artigo artigo;

    ImageView imgPrimeiraImagemDetalhesMarketplace, imgPerfilMarketplace;
    TextView txtDetailsNomeMarketPlace, txdDetailsMarcaMarketplace, txtPrecoDetailsMarketplace,
            txtCondicaoDetailsMarketplace, txtDetailsSizeMarketplace, txtDetailsDescricaoMarketplace,
            txtQuantidadeReviewsArtigoMarketplace;
    RatingBar ratingBar;

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
        txtQuantidadeReviewsArtigoMarketplace = findViewById(R.id.txtQuantidadeReviewsArtigoMarketplace);
        imgPerfilMarketplace = findViewById(R.id.imgPerfilMarketplace);
        ratingBar = findViewById(R.id.ratingBarMarketPlace);

        int id = getIntent().getIntExtra("ID", 0);
        artigo = Singleton.getInstance(this).getArtigo(id);

        if(artigo != null){
            carregarArtigo();
        }
        else {
            Toast.makeText(this, "item unavailable", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
    private void carregarArtigo(){
        //txtDetailsNomeMarketPlace.setText(artigo.getNome());
        txtPrecoDetailsMarketplace.setText(artigo.getPrecoFormatado());
        txtCondicaoDetailsMarketplace.setText(artigo.getEstado());
        txdDetailsMarcaMarketplace.setText(artigo.getMarca());
        txtDetailsSizeMarketplace.setText(artigo.getTamanho());
        txtDetailsDescricaoMarketplace.setText(artigo.getDescricao());
        txtQuantidadeReviewsArtigoMarketplace.setText( String.valueOf(artigo.getPerfil().getQuantidadeAvaliacoes() + " Reviews"));
        txtQuantidadeReviewsArtigoMarketplace.setText( String.valueOf(artigo.getPerfil().getQuantidadeAvaliacoes() + " Reviews"));
        ratingBar.setRating(artigo.getPerfil().getMediaAvaliacoes());

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
        Singleton.getInstance(this).setCartListeneristener(this);
        Singleton.getInstance(this).addToCartAPI(Utils.getToken(this), artigo.getId(), this);
    }

    @Override
    public void onAddCarrinho(Carrinho carrinho) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("added_to_cart", true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void backMenuActivity(View view) {
        finish();
    }
}