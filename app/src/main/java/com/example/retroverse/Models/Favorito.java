package com.example.retroverse.Models;

import static com.example.retroverse.Singleton.Singleton.baseUrl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Favorito {

    private ArrayList<Artigo> artigos = new ArrayList<>();

    public ArrayList<Artigo> getArtigos() {
        return artigos;
    }

    public void setArtigos(ArrayList<Artigo> artigos) {
        this.artigos = artigos;
    }
}
