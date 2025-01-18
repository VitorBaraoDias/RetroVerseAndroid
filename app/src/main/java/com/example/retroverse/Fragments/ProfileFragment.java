package com.example.retroverse.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Activities.ArtigoDetailsLojaActivity;
import com.example.retroverse.Activities.MeuArtigoDetailsActivity;
import com.example.retroverse.Adapters.ListaArtigosAdapter;
import com.example.retroverse.HistoricoActivity;
import com.example.retroverse.Listeners.PerfilRefreshListener;
import com.example.retroverse.MainActivity;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Models.Perfil;
import com.example.retroverse.R;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements PerfilRefreshListener, ListaArtigosAdapter.OnItemClickListener, EditProfileFragment.OnProfileEditListener {

    private View rootView;
    private TextView tvUsername, tvDescricao, tvAvaliacoesCount, tvLocalizacao;
    private ImageView ivFotoPerfil, btnEditProfile, btnLogout;
    private RatingBar ratingBar;
    RecyclerView recyclerView;
    ListaArtigosAdapter listaArtigosAdapter;

    ImageView btnMyOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        tvUsername = rootView.findViewById(R.id.tvProfileUsername);
        tvDescricao = rootView.findViewById(R.id.tvProfileDescription);
        tvLocalizacao = rootView.findViewById(R.id.tvProfileLocation);
        tvAvaliacoesCount = rootView.findViewById(R.id.tvQuantidadeAvaliacoes);
        ratingBar = rootView.findViewById(R.id.profileRatingBar);
        ivFotoPerfil = rootView.findViewById(R.id.ivProfileImgListaFavoritos);
        recyclerView = rootView.findViewById(R.id.recyclerViewMyItems);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        btnLogout = rootView.findViewById(R.id.btnLogout);
        btnMyOrders = rootView.findViewById(R.id.btnMyOrders);


        Singleton.getInstance(getActivity()).setPerfilRefreshListener(this);
        Singleton.getInstance(getActivity()).getPerfilAPI(Utils.getToken(getContext()), getActivity());


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfileDialog();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();

            }
        });

        btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoricoActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }


    @Override
    public void onRefreshPerfil(Perfil perfil) {
        if (perfil != null) {
            // Atualizar os campos de texto
            tvDescricao.setText(perfil.getDescricao() != null ? perfil.getDescricao() : "Descrição não disponível");
            tvUsername.setText(perfil.getUsername() != null ? perfil.getUsername() : "Username não disponível");
            tvLocalizacao.setText(perfil.getMorada() != null ? perfil.getMorada() : "Localizacao não disponível");

            int quantidadeAvaliacoes = perfil.getQuantidadeAvaliacoes();
            String avaliacoesText;

            if (quantidadeAvaliacoes == 0) {
                avaliacoesText = "No reviews";
            } else {
                avaliacoesText = quantidadeAvaliacoes + " Reviews";
            }

            tvAvaliacoesCount.setText(avaliacoesText);

            float mediaAvaliacoes = perfil.getMediaAvaliacoes();

            ratingBar.setRating(mediaAvaliacoes);

            String fotoperfilUrl = perfil.getFotoperfil();

                Glide.with(this)
                        .load(fotoperfilUrl)
                        .placeholder(R.drawable.profile_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivFotoPerfil);

                ivFotoPerfil.setTag(fotoperfilUrl);

            setAdapter(perfil.getArtigosPublicados());
        }
    }

    private void setAdapter(ArrayList<Artigo> artigosUser) {
        if (listaArtigosAdapter == null) {

            listaArtigosAdapter = new ListaArtigosAdapter(artigosUser, getActivity(),false);

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

    private void logoutUser() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Artigo artigo, int position) {
            Intent intent = new Intent(getContext(), MeuArtigoDetailsActivity.class);
            intent.putExtra("ID",(int) artigo.getId());
            startActivity(intent);
    }

    public void openEditProfileDialog() {
        String descricao = tvDescricao.getText().toString();
        String localizacao = tvLocalizacao.getText().toString();
        String fotoUrl = ivFotoPerfil.getTag().toString();


        EditProfileFragment editProfileDialogFragment = EditProfileFragment.newInstance(descricao, localizacao, fotoUrl);

        editProfileDialogFragment.setTargetFragment(this, 0);
        editProfileDialogFragment.show(getParentFragmentManager(), "EDIT_PROFILE_DIALOG");
    }


    @Override
    public void onProfileEdited(String descricao, String localizacao, String fotoUrl) {
        Toast.makeText(getContext(), "Perfil atualizado", Toast.LENGTH_SHORT).show();
    }
}