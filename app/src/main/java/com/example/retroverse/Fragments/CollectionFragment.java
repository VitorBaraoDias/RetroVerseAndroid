package com.example.retroverse.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.retroverse.Models.Categoria;
import com.example.retroverse.Models.Estado;
import com.example.retroverse.Models.Marca;
import com.example.retroverse.Models.Tamanho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CollectionFragment extends Fragment implements ListaArtigosListener, RefreshArtigosListener, ListaArtigosAdapter.OnItemClickListener, ListaArtigosAdapter.OnItemLikeClickListener, CartCountRefreshListener {

    View view;
    RecyclerView recyclerView;
    TextView txtQuantCartHome;
    ImageView imgCartHome;
    private ListaArtigosAdapter listaArtigosAdapter, listaArtigosAdapterPremium;

    ArrayList<Artigo> listaArtigos, listaArtigoPremium;
    TextInputLayout eArtigoPesquisar;

    AutoCompleteTextView dropdown_menuMarcaPesquisar, dropdown_menuTipoPesquisar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_collection, container, false);

        txtQuantCartHome = view.findViewById(R.id.txtQuantCartHome);
        imgCartHome = view.findViewById(R.id.imgCartHome);
        recyclerView = view.findViewById(R.id.recyclerViewMyItems);
        dropdown_menuMarcaPesquisar = view.findViewById(R.id.dropdown_menuMarcaPesquisar);
        dropdown_menuTipoPesquisar = view.findViewById(R.id.dropdown_menuTipoPesquisar);
        eArtigoPesquisar = view.findViewById(R.id.eArtigoPesquisar);


        if (getContext() != null) {
            Singleton.getInstance(getActivity()).setArtigosListener(this);
            Singleton.getInstance(getActivity()).setArtigosRefreshListener(this);
            Singleton.getInstance(getActivity()).setCartCountRefreshListener(this);

            Singleton.getInstance(getActivity()).getAllArtigosAPI(Utils.getToken(getActivity()), null, null, null, null, null, getActivity());
            Singleton.getInstance(getActivity()).getCartApi(Utils.getToken(getActivity()), getContext());
        }


        carregarInfoDropDown();
        imgCartHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CarrinhoActivity.class);
            startActivity(intent);
        });



        eArtigoPesquisar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesquisar();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dropdown_menuMarcaPesquisar.setOnItemClickListener((parent, view, position, id) -> {
            pesquisar();
        });
        dropdown_menuTipoPesquisar.setOnItemClickListener((parent, view, position, id) -> {
            pesquisar();
        });
        return view;
    }

    public void openCartActivity(View view) {
        Intent intent = new Intent(getContext(), CarrinhoActivity.class);
        startActivity(intent);
    }

    private void setAdapter() {
        if (listaArtigosAdapter != null) {
            listaArtigosAdapter.updateData(this.listaArtigos);
            listaArtigosAdapter.notifyDataSetChanged();
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
    private void carregarInfoDropDown(){
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Singleton.getInstance(getActivity()).getCategorias());
        dropdown_menuTipoPesquisar.setAdapter(adapter);

        ArrayAdapter<Marca> adapterMarca = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Singleton.getInstance(getActivity()).getMarcas());
        dropdown_menuMarcaPesquisar.setAdapter(adapterMarca);
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
    private void pesquisar(){
        String nome = eArtigoPesquisar.getEditText().getText().toString();
        String marca = dropdown_menuMarcaPesquisar.getText().toString();
        String tipo = dropdown_menuTipoPesquisar.getText().toString();
        Toast.makeText(getActivity(), marca, Toast.LENGTH_SHORT).show();
        setAdapter();
    }
}
