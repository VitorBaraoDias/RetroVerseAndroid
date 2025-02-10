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
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Listeners.RefreshEstadoLike;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ArtigoDetailsLojaActivity extends AppCompatActivity implements ListaArtigosAdapter.OnItemClickListener, CartListener, RefreshEstadoLike {

    RecyclerView recyclerViewRelatedItensLoja;
    ListaArtigosAdapter listaArtigosAdapter;
    Artigo artigo;
    ImageView imgLikeLojaPlaceDetails, imgInfoPremiumStatusArtigo;
    TextView txtDetailsNomeLoja, txdDetailsMarcaLoja, txtPrecoDetailsLoja,
            txtCondicaoDetailsLoja, txtDetailsSizeLoja, txtDetailsDescricaoLoja, txtDetalhesFotoartigoLoja;

    ViewPager2 viewPager;

    public static final int ADD = 100, EDIT = 200, DELETE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_artigo_details_loja);

        recyclerViewRelatedItensLoja = findViewById(R.id.recyclerViewRelatedItensLoja);
        txtDetailsNomeLoja = findViewById(R.id.txtDetailsNomeLoja);
        txdDetailsMarcaLoja = findViewById(R.id.txdDetailsMarcaLoja);
        txtPrecoDetailsLoja = findViewById(R.id.txtPrecoDetailsLoja);
        txtCondicaoDetailsLoja = findViewById(R.id.txtCondicaoDetailsLoja);
        txtDetailsSizeLoja = findViewById(R.id.txtDetailsSizeLoja);
        txtDetailsDescricaoLoja = findViewById(R.id.txtDetailsDescricaoLoja);
        txtDetalhesFotoartigoLoja = findViewById(R.id.txtDetalhesFotoartigoLoja);
        imgInfoPremiumStatusArtigo = findViewById(R.id.imgInfoPremiumStatusArtigo);
        imgLikeLojaPlaceDetails = findViewById(R.id.imgLikeLojaPlaceDetails);
        viewPager = findViewById(R.id.viewPagerDetalhesLoja);



        int id = getIntent().getIntExtra("ID", 0);
        artigo = Singleton.getInstance(this).getArtigo(id);
        Singleton.getInstance(this).setRefreshEstadoLike(this);


        carregarArtigo();
        setImageLike(artigo);

        setAdapter(Singleton.getInstance(this).filterNonPremiumArticles(id));
    }


    private void carregarArtigo(){
        txtDetailsNomeLoja.setText(artigo.getNome());
        txtPrecoDetailsLoja.setText(artigo.getPrecoFormatado());
        txtCondicaoDetailsLoja.setText(artigo.getEstado());
        txdDetailsMarcaLoja.setText(artigo.getMarca());
        txtDetailsSizeLoja.setText(artigo.getTamanho());
        txtDetailsDescricaoLoja.setText(artigo.getDescricao());
        txtDetalhesFotoartigoLoja.setText(String.valueOf(artigo.getFotos().size()));

        List<String> images = artigo.getFotos();
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, images, artigo.getBaseUrlFoto());
        viewPager.setAdapter(adapter);

        if(artigo.isPremium()){
            imgInfoPremiumStatusArtigo.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(ArrayList<Artigo> artigos) {
        if (listaArtigosAdapter == null) {
            // Inicializa os adaptadores com as listas separadas

            listaArtigosAdapter = new ListaArtigosAdapter(artigos, getApplicationContext(), true);
            listaArtigosAdapter.setOnItemClickListener(this);

            ///normal
            recyclerViewRelatedItensLoja.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewRelatedItensLoja.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRelatedItensLoja.setAdapter(listaArtigosAdapter);

        } else {
            listaArtigosAdapter.notifyDataSetChanged();
        }
    }

    //
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
    @Override
    public void onAddCarrinho(Carrinho carrinho) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("added_to_cart", true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    public void addToCart(View view) {
        Singleton.getInstance(this).setCartListeneristener(this);
        Singleton.getInstance(this).addToCartAPI(Utils.getToken(this), artigo.getId(), this);
    }

    public void finishaActivity(View view) {
        finish();
    }

    public void addLikeItemLoja(View view) {
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
            imgLikeLojaPlaceDetails.setImageResource(R.drawable.group_170);
        } else {
            imgLikeLojaPlaceDetails.setImageResource(R.drawable.outline_heart_plus_24);
        }
    }
}