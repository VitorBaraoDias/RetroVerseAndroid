package com.example.retroverse.Listeners;

import android.content.Context;

import com.example.retroverse.Models.Artigo;

import java.util.ArrayList;

public interface ListaArtigosListener {
    void onRefreshListaArtigos(ArrayList<Artigo> listaArtigos);

}
