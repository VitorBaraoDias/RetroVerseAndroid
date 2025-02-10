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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Adapters.ImageSliderAdapter;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Listeners.RefreshEstadoLike;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ArtigoMarketPlaceDetailsActivity extends AppCompatActivity implements CartListener, RefreshEstadoLike, ListaArtigosAdapter.OnItemClickListener {

    RecyclerView recyclerViewRelatedItenS;
    Artigo artigo;
    ListaArtigosAdapter listaArtigosAdapter;
    ImageView imgPrimeiraImagemDetalhesMarketplace, imgPerfilMarketplace, imgLikeMarketPlaceDetails;
    TextView txtDetailsNomeMarketPlace, txdDetailsMarcaMarketplace, txtPrecoDetailsMarketplace,
            txtCondicaoDetailsMarketplace, txtDetailsSizeMarketplace, txtDetailsDescricaoMarketplace,
            txtQuantidadeReviewsArtigoMarketplace, txtUsernamePerfiMarketplace,txtDetalhesFotoartigoMarketplace;
    RatingBar ratingBar;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_artigo_market_place_details);


        recyclerViewRelatedItenS = findViewById(R.id.recyclerViewRelatedItensMarketplace);
        txtDetailsNomeMarketPlace = findViewById(R.id.txtDetailsNomeMarketplace);
        txdDetailsMarcaMarketplace = findViewById(R.id.txdDetailsMarcaMarketplace);
        txtPrecoDetailsMarketplace = findViewById(R.id.txtPrecoDetailsMarketplace);
        txtDetalhesFotoartigoMarketplace = findViewById(R.id.txtDetalhesFotoartigoMarketplace);
        txtCondicaoDetailsMarketplace = findViewById(R.id.txtCondicaoDetailsMarketplace);
        txtDetailsSizeMarketplace = findViewById(R.id.txtDetailsSizeMarketplace);
        txtDetailsDescricaoMarketplace = findViewById(R.id.txtDetailsDescricaoMartkeplace);
        txtQuantidadeReviewsArtigoMarketplace = findViewById(R.id.txtQuantidadeReviewsArtigoMarketplace);
        imgPerfilMarketplace = findViewById(R.id.imgPerfilMarketplace);
        imgLikeMarketPlaceDetails = findViewById(R.id.imgLikeMarketPlaceDetails);
        txtUsernamePerfiMarketplace = findViewById(R.id.txtUsernamePerfiMarketplace);
        ratingBar = findViewById(R.id.ratingBarMarketPlace);
        viewPager = findViewById(R.id.viewPagerDetalhesLoja);


        int id = getIntent().getIntExtra("ID", 0);
        artigo = Singleton.getInstance(this).getArtigo(id);

        if(artigo != null){
            carregarArtigo();

            setAdapter(Singleton.getInstance(this).filterNonPremiumArticles(id));

            setImageLike(artigo);
            Singleton.getInstance(this).setRefreshEstadoLike(this);
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
        txtUsernamePerfiMarketplace.setText(artigo.getPerfil().getUsername());
        txtDetalhesFotoartigoMarketplace.setText(String.valueOf(artigo.getFotos().size()));

        Glide.with(this)
                .load(artigo.getPerfil().getFotoperfil())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPerfilMarketplace);

        List<String> images = artigo.getFotos();
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, images, artigo.getBaseUrlFoto());
        viewPager.setAdapter(adapter);
    }

    public void addToCart(View view) {
        Singleton.getInstance(this).setCartListeneristener(this);
        Singleton.getInstance(this).addToCartAPI(Utils.getToken(this), artigo.getId(), this);
    }
    private void setAdapter(ArrayList<Artigo> artigos) {
        if (listaArtigosAdapter == null) {
            // Inicializa os adaptadores com as listas separadas

            listaArtigosAdapter = new ListaArtigosAdapter(artigos, getApplicationContext(), true);
            listaArtigosAdapter.setOnItemClickListener(this);

            ///normal
            recyclerViewRelatedItenS.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewRelatedItenS.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRelatedItenS.setAdapter(listaArtigosAdapter);

        } else {
            listaArtigosAdapter.notifyDataSetChanged();
        }
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

    public void addLikeItemMarketplace(View view) {
        if(artigo.isLiked()){
            Singleton.getInstance(this).removeFavoritoAPI(Utils.getToken(this), artigo, this, false);
        }
        else{
            Singleton.getInstance(this).addFavoritoAPI(Utils.getToken(this), artigo.getId(), this);
        }
    }

    @Override
    public void onRefreshEstadoLike(Artigo artigo) {
        this.artigo = artigo;
        setImageLike(artigo);

    }
    private void setImageLike(Artigo artigo){
        if (artigo.isLiked()) {
            imgLikeMarketPlaceDetails.setImageResource(R.drawable.group_170);
        } else {
            imgLikeMarketPlaceDetails.setImageResource(R.drawable.outline_heart_plus_24);
        }
    }

    @Override
    public void onItemClick(Artigo artigo, int position) {
        if(artigo.getTipoArtigo().equals("LOJA")){
            Intent intent = new Intent(this, ArtigoDetailsLojaActivity.class);
            intent.putExtra("ID",(int) artigo.getId());
            startActivity(intent);
        }
        else if(artigo.getTipoArtigo().equals("MARKETPLACE")){
            Intent intent = new Intent(this, ArtigoMarketPlaceDetailsActivity.class);
            intent.putExtra("ID",(int) artigo.getId());
            startActivity(intent);
        }
        finish();
    }
}