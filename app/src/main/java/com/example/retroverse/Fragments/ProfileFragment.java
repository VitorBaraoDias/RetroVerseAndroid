package com.example.retroverse.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Activities.ArtigoDetailsLojaActivity;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.Listeners.ListaArtigosListener;
import com.example.retroverse.Listeners.PerfilRefreshListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Models.Perfil;
import com.example.retroverse.R;
import com.example.retroverse.Utils;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements PerfilRefreshListener, ListaArtigosAdapter.OnItemClickListener {

    private View rootView;
    private TextView tvUsername, tvDescricao, tvSaldo, tvSaldoPendente;
    private ImageView ivFotoPerfil;
    RecyclerView recyclerView;
    ListaArtigosAdapter listaArtigosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        tvUsername = rootView.findViewById(R.id.tvProfileUsername);
        tvDescricao = rootView.findViewById(R.id.tvProfileDescription);
        ivFotoPerfil = rootView.findViewById(R.id.ivProfileImg);
        recyclerView = rootView.findViewById(R.id.recyclerViewMyItems);

        Singleton.getInstance(getActivity()).setPerfilRefreshListener(this);
        Singleton.getInstance(getActivity()).getPerfilAPI(Utils.getToken(getActivity()), getActivity());

        return rootView;
    }


    @Override
    public void onRefreshPerfil(Perfil perfil) {
        if (perfil != null) {
            // Atualizar os campos de texto
            tvDescricao.setText(perfil.getDescricao() != null ? perfil.getDescricao() : "Descrição não disponível");
            tvUsername.setText(perfil.getUsername() != null ? perfil.getUsername() : "Username não disponível");

            String fotoperfilUrl = perfil.getFotoperfil();

                Glide.with(this)
                        .load(fotoperfilUrl)
                        .placeholder(R.drawable.profile_default_image)  // Imagem padrão
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivFotoPerfil);
            setAdapter(perfil.getArtigosPublicados());
        }
    }

    private void setAdapter(ArrayList<Artigo> artigosUser) {
        if (listaArtigosAdapter == null) {

            listaArtigosAdapter = new ListaArtigosAdapter(artigosUser, getActivity());

            listaArtigosAdapter.setOnItemClickListener(this);

            ///normal
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(listaArtigosAdapter);
            ///normal

        } else {
            listaArtigosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(Artigo artigo, int position) {

        /*if (artigo.getTipoArtigo().equals("LOJA")) {
            Intent intent = new Intent(getContext(), ArtigoDetailsLojaActivity.class);
            intent.putExtra("ID", (int) artigo.getId());
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "nao é tipo loja", Toast.LENGTH_SHORT).show();
        }*/
    }

}