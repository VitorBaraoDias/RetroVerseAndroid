package com.example.retroverse.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.ArtigoActivities.ArtigoDetailsLojaActivity;
import com.example.retroverse.Listeners.ListaArtigosListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ListaArtigosListener, ListaArtigosAdapter.OnItemClickListener {

    View view;
    RecyclerView recyclerView, recyclerViewLatestPremiumDrops;
    private ArrayList<Artigo> listaArtigos = new ArrayList<>();
    private ListaArtigosAdapter listaArtigosAdapter, listaArtigosAdapterPremium;

    public static final int ADD = 100, EDIT = 200, VIEW = 201, DELETE = 300;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLastDrops);
        recyclerViewLatestPremiumDrops = view.findViewById(R.id.recyclerViewLatestPremiumDrops);

        // Inicializa o adaptador com a lista vazia

        Singleton.getInstance(getActivity()).setArtigosListener(this);
        Singleton.getInstance(getActivity()).getAllArtigosAPI(Utils.getToken(getActivity()), null, null, null, null, null, getActivity());

// Configura o listener de clique no adaptador não premium

        return view;
    }

    @Override
    public void onRefreshListaArtigos(ArrayList<Artigo> listaArtigos) {
        if (listaArtigos != null) {
            this.listaArtigos = listaArtigos;



            setAdapter(Singleton.getInstance(getActivity()).filterPremiumArticles(),
                    Singleton.getInstance(getActivity()).filterNonPremiumArticles());
        }
    }
    private void setAdapter(ArrayList<Artigo> artigosPremium, ArrayList<Artigo> artigosNaoPremium) {
        if (listaArtigosAdapter == null) {
            // Inicializa os adaptadores com as listas separadas

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
            startActivityForResult(intent, EDIT);
        }
        else {
            Toast.makeText(getActivity(), "nao é tipo loja", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

            switch (requestCode){
                case ADD:
                    Toast.makeText(getContext(), "Livro Adicionado", Toast.LENGTH_LONG).show();
                    break;
                case VIEW:
                    Toast.makeText(getContext(), "Livro visto", Toast.LENGTH_LONG).show();
                    break;
                case EDIT:
                    Toast.makeText(getContext(), "Livro Editado", Toast.LENGTH_LONG).show();
                    break;
                case DELETE:
                    Toast.makeText(getContext(), "Livro Eliminado", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
