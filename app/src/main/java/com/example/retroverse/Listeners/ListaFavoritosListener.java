package com.example.retroverse.Listeners;

import com.example.retroverse.Models.Artigo;

import java.util.ArrayList;

public interface ListaFavoritosListener {
    void onRefreshListaFavoritos(ArrayList<Artigo> favoritos);

}
