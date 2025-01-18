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
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;


public class ArtigoDetailsLojaActivity extends AppCompatActivity implements ListaArtigosAdapter.OnItemClickListener, CartListener{

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
        setContentView(R.layout.activity_artigo_details_loja);

        recyclerViewRelatedItensLoja = findViewById(R.id.recyclerViewRelatedItensLoja);
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
        setAdapter(Singleton.getInstance(this).filterNonPremiumArticles(id));
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
}