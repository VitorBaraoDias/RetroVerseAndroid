package com.example.retroverse.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Activities.ArtigoDetailsLojaActivity;
import com.example.retroverse.Activities.CarrinhoActivity;
import com.example.retroverse.Listeners.CartCountRefreshListener;
import com.example.retroverse.Listeners.ListaArtigosListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements ListaArtigosListener, ListaArtigosAdapter.OnItemClickListener, CartCountRefreshListener {

    View view;
    RecyclerView recyclerView, recyclerViewLatestPremiumDrops;
    TextView txtQuantCartHome;
    ImageView imgCartHome;
    private ListaArtigosAdapter listaArtigosAdapter, listaArtigosAdapterPremium;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        txtQuantCartHome = view.findViewById(R.id.txtQuantCartHome);
        imgCartHome = view.findViewById(R.id.imgCartHome);
        recyclerView = view.findViewById(R.id.recyclerViewMyItems);
        recyclerViewLatestPremiumDrops = view.findViewById(R.id.recyclerViewLatestPremiumDrops);


        // Inicializa o adaptador com a lista vazia
        Singleton.getInstance(getActivity()).setArtigosListener(this);
        Singleton.getInstance(getActivity()).setCartCountRefreshListener(this);

        // Configura o listener de clique no adaptador não premium
        Singleton.getInstance(getActivity()).getAllArtigosAPI(Utils.getToken(getActivity()), null, null, null, null, null, getActivity());
        Singleton.getInstance(getActivity()).getCartApi(Utils.getToken(getActivity()), getContext());


        imgCartHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CarrinhoActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onRefreshListaArtigos(ArrayList<Artigo> listaArtigos) {
        if (listaArtigos != null) {
            setAdapter(Singleton.getInstance(getActivity()).filterPremiumArticles(), Singleton.getInstance(getActivity()).filterNonPremiumArticles(0));
        }
    }
    private void setAdapter(ArrayList<Artigo> artigosPremium, ArrayList<Artigo> artigosNaoPremium) {
        if (listaArtigosAdapter == null) {

            listaArtigosAdapter = new ListaArtigosAdapter(artigosNaoPremium, getActivity());
            listaArtigosAdapterPremium = new ListaArtigosAdapter(artigosPremium, getActivity());

            listaArtigosAdapter.setOnItemClickListener(this);

            ///normal
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(listaArtigosAdapter);

            ///normal

            ///premium
            recyclerViewLatestPremiumDrops.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewLatestPremiumDrops.setItemAnimator(new DefaultItemAnimator());
            recyclerViewLatestPremiumDrops.setAdapter(listaArtigosAdapterPremium);
            ///premium

        } else {
            listaArtigosAdapter.notifyDataSetChanged();
            listaArtigosAdapterPremium.notifyDataSetChanged();
        }
    }
    @Override
    public void onItemClick(Artigo artigo, int position) {

        if(artigo.getTipoArtigo().equals("LOJA")){
            Intent intent = new Intent(getContext(), ArtigoDetailsLojaActivity.class);
            intent.putExtra("ID",(int) artigo.getId());
            startActivity(intent);
        }
        else {
            Toast.makeText(getActivity(), "nao é tipo loja", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshCarrinho(Carrinho carrinho) {
        txtQuantCartHome.setText(String.valueOf(carrinho.getLinhasCarrinho().size()));
    }

    public void openCartActivity(View view) {
        Intent intent = new Intent(getContext(), CarrinhoActivity.class);
        startActivity(intent);
    }
}
