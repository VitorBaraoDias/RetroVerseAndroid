package com.example.retroverse.bd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    private static final String DB_NAME = "retroverse";
    private static final int DB_VERSION = 1;

    private static final String TABLE_FAVORITOS_ARTIGOS = "favoritos_artigos";

    // Colunas
    private static final String ID = "id";
    private static final String ID_ARTIGO = "idartigo";
    private static final String TOKEN = "token";

    private static final String IDPERFIL = "idperfil";
    private static final String NOME = "nome";
    private static final String DESCRICAO = "descricao";
    private static final String PRECOANUNCIO = "precoanuncio";
    private static final String COMISSAO = "comissao";
    private static final String ESTADO = "estado";
    private static final String MARCA = "marca";
    private static final String CATEGORIA = "categoria";
    private static final String TAMANHO = "tamanho";
    private static final String TIPOARTIGO = "tipoartigo";
    private static final String ATIVO = "ativo";
    private static final String PREMIUM = "premium";
    private static final String ISLIKED = "isLiked";

    // Tabela de fotos (mantida separada, já que um artigo pode ter várias fotos)
    private static final String TABLE_FOTOS = "fotosartigos";
    private static final String FOTO_URL = "url";
    private static final String IDARTIGO = "idartigo";

    private final SQLiteDatabase db;


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Criação da tabela unificada
        String createFavoritosArtigosTable = "CREATE TABLE " + TABLE_FAVORITOS_ARTIGOS + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TOKEN + " TEXT, "
                + IDPERFIL + " INTEGER NOT NULL, "
                + ID_ARTIGO + " INTEGER NOT NULL, "
                + NOME + " TEXT, "
                + DESCRICAO + " TEXT, "
                + PRECOANUNCIO + " REAL, "
                + COMISSAO + " REAL, "
                + ESTADO + " TEXT, "
                + MARCA + " TEXT, "
                + CATEGORIA + " TEXT, "
                + TAMANHO + " TEXT, "
                + TIPOARTIGO + " TEXT, "
                + ATIVO + " TEXT, "
                + PREMIUM + " INTEGER, "
                + ISLIKED + " INTEGER);";
        sqLiteDatabase.execSQL(createFavoritosArtigosTable);

        // Criação da tabela de fotos
        String createFotosTable = "CREATE TABLE " + TABLE_FOTOS + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IDARTIGO + " INTEGER NOT NULL, "
                + FOTO_URL + " TEXT);";
        sqLiteDatabase.execSQL(createFotosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS_ARTIGOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOTOS);
        onCreate(sqLiteDatabase);
    }
    // Método para adicionar um artigo favorito ao banco de dados
    public void adicionarFavoritoArtigo(Artigo artigo) {
        ContentValues values = new ContentValues();
        values.put(TOKEN, Utils.getToken(context));
        values.put(IDPERFIL, artigo.getPerfil().getId());
        values.put(ID_ARTIGO, artigo.getId());
        values.put(NOME, artigo.getNome());
        values.put(DESCRICAO, artigo.getDescricao());
        values.put(PRECOANUNCIO, artigo.getPrecoAnuncio());
        values.put(COMISSAO, artigo.getComissao());
        values.put(ESTADO, artigo.getEstado());
        values.put(MARCA, artigo.getMarca());
        values.put(CATEGORIA, artigo.getCategoria());
        values.put(TAMANHO, artigo.getTamanho());
        values.put(TIPOARTIGO, artigo.getTipoArtigo());
        values.put(ATIVO, artigo.getAtivo());
        values.put(PREMIUM, artigo.isPremium() ? 1 : 0);
        values.put(ISLIKED, artigo.isLiked() ? 1 : 0);

        long artigoId = db.insert(TABLE_FAVORITOS_ARTIGOS, null, values);

        // Adiciona as fotos relacionadas ao artigo
        for (String foto : artigo.getFotos()) {
            ContentValues fotoValues = new ContentValues();
            fotoValues.put(IDARTIGO, artigoId);
            fotoValues.put(FOTO_URL, foto);
            db.insert(TABLE_FOTOS, null, fotoValues);
        }
    }

    public void removerAllArtigosBD(){
        this.db.delete(TABLE_FAVORITOS_ARTIGOS, null, null);
    }
    public boolean removerFavoritoArtigo(int id) {
        int rowsDeleted = db.delete(TABLE_FAVORITOS_ARTIGOS, ID_ARTIGO + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Método para buscar todos os favoritos
    @SuppressLint("Range")
    public ArrayList<Artigo> getTodosFavoritos(String token) {
        ArrayList<Artigo> artigos = new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_FAVORITOS_ARTIGOS,
                null,
                TOKEN + " = ?",
                new String[]{String.valueOf(token)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Artigo artigo = new Artigo();
                artigo.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                artigo.setNome(cursor.getString(cursor.getColumnIndex(NOME)));
                artigo.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                artigo.setPrecoAnuncio(cursor.getDouble(cursor.getColumnIndex(PRECOANUNCIO)));
                artigo.setComissao(cursor.getDouble(cursor.getColumnIndex(COMISSAO)));
                artigo.setEstado(cursor.getString(cursor.getColumnIndex(ESTADO)));
                artigo.setMarca(cursor.getString(cursor.getColumnIndex(MARCA)));
                artigo.setCategoria(cursor.getString(cursor.getColumnIndex(CATEGORIA)));
                artigo.setTamanho(cursor.getString(cursor.getColumnIndex(TAMANHO)));
                artigo.setTipoArtigo(cursor.getString(cursor.getColumnIndex(TIPOARTIGO)));
                artigo.setAtivo(cursor.getString(cursor.getColumnIndex(ATIVO)));
                artigo.setPremium(cursor.getInt(cursor.getColumnIndex(PREMIUM)) == 1);
                artigo.setLiked(cursor.getInt(cursor.getColumnIndex(ISLIKED)) == 1);
                artigo.setFotos(getFotosByArtigoId(artigo.getId()));
                artigos.add(artigo);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return artigos;
    }

    // Método para buscar fotos de um artigo
    @SuppressLint("Range")
    private List<String> getFotosByArtigoId(int idartigo) {
        List<String> fotos = new ArrayList<>();
        Cursor cursor = db.query(TABLE_FOTOS, new String[]{FOTO_URL}, ID + " = ?", new String[]{String.valueOf(idartigo)}, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            fotos.add(cursor.getString(cursor.getColumnIndex(FOTO_URL)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return fotos;
    }
}
