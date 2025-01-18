package com.example.retroverse.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Activities.ArtigoDetailsLojaActivity;
import com.example.retroverse.Activities.ArtigoMarketPlaceDetailsActivity;
import com.example.retroverse.Activities.CarrinhoActivity;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Listeners.CartCountRefreshListener;
import com.example.retroverse.Listeners.ListaArtigosListener;
import com.example.retroverse.Listeners.RefreshArtigosListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;

public class CollectionFragment extends Fragment implements ListaArtigosListener, RefreshArtigosListener, ListaArtigosAdapter.OnItemClickListener, ListaArtigosAdapter.OnItemLikeClickListener, CartCountRefreshListener {

    View view;
    RecyclerView recyclerView;
    TextView txtQuantCartHome;
    ImageView imgCartHome;
    private ListaArtigosAdapter listaArtigosAdapter, listaArtigosAdapterPremium;

    ArrayList<Artigo> listaArtigos, listaArtigoPremium;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_collection, container, false);

        txtQuantCartHome = view.findViewById(R.id.txtQuantCartHome);
        imgCartHome = view.findViewById(R.id.imgCartHome);
        recyclerView = view.findViewById(R.id.recyclerViewMyItems);


        if (getContext() != null) {
            Singleton.getInstance(getActivity()).setArtigosListener(this);
            Singleton.getInstance(getActivity()).setArtigosRefreshListener(this);
            Singleton.getInstance(getActivity()).setCartCountRefreshListener(this);

            Singleton.getInstance(getActivity()).getAllArtigosAPI(Utils.getToken(getActivity()), null, null, null, null, null, getActivity());
            Singleton.getInstance(getActivity()).getCartApi(Utils.getToken(getActivity()), getContext());
        }



        imgCartHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CarrinhoActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public void openCartActivity(View view) {
        Intent intent = new Intent(getContext(), CarrinhoActivity.class);
        startActivity(intent);
    }

    private void setAdapter() {
        if (listaArtigosAdapter != null && listaArtigosAdapterPremium != null) {
            listaArtigosAdapter.updateData(this.listaArtigos);
            listaArtigosAdapterPremium.updateData(this.listaArtigoPremium);
            listaArtigosAdapter.notifyDataSetChanged();
            listaArtigosAdapterPremium.notifyDataSetChanged();
        } else {
            // Criação do adaptador (primeira inicialização)
            listaArtigosAdapter = new ListaArtigosAdapter(this.listaArtigos, getActivity(), true);

            listaArtigosAdapter.setOnItemClickListener(this);
            listaArtigosAdapter.setOnItemLikeClickListener(this);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(listaArtigosAdapter);

        }

    }
    @Override
    public void onGetListaArtigos(ArrayList<Artigo> listaArtigos) {
        if (listaArtigos != null) {
            this.listaArtigos = Singleton.getInstance(getActivity()).filterNonPremiumArticles(0);
            this.listaArtigoPremium = Singleton.getInstance(getActivity()).filterPremiumArticles();

            setAdapter();
        }
    }
    @Override
    public void onItemClick(Artigo artigo, int position) {

        if(artigo.getTipoArtigo().equals("LOJA")){
            Intent intent = new Intent(getContext(), ArtigoDetailsLojaActivity.class);
            intent.putExtra("ID",(int) artigo.getId());
            startActivity(intent);
        }
        else if(artigo.getTipoArtigo().equals("MARKETPLACE")){
            Intent intent = new Intent(getContext(), ArtigoMarketPlaceDetailsActivity.class);
            intent.putExtra("ID",(int) artigo.getId());
            startActivity(intent);        }
    }
    @Override
    public void onRefreshCarrinho(Carrinho carrinho) {

        txtQuantCartHome.setText(String.valueOf(carrinho.getLinhasCarrinho().size()));
    }
    @Override
    public void onItemLikeClick(Artigo artigo, int position) {

        if(artigo.isLiked()){ //SE FOR TRUE IRA ELIMINAR O FAVORITO
            Singleton.getInstance(getActivity()).removeFavoritoAPI(Utils.getToken(getActivity()), artigo, getActivity(), false);

        }
        else{
            Singleton.getInstance(getActivity()).addFavoritoAPI(Utils.getToken(getActivity()), artigo.getId(), getActivity());
        }
    }

    @Override
    public void onRefreshListaArtigos() {
        if (getContext() != null) {
            Singleton.getInstance(getActivity()).getAllArtigosAPI(Utils.getToken(getContext()), null, null, null, null, null, getActivity());
        }
    }
}
