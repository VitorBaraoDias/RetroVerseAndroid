package com.example.retroverse.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FavoritoDBHelper extends SQLiteOpenHelper {

    //nome da base de dados
    private static final String DB_NAME="retroverse";
    //nome da(s) tabela(s)
    private static final String FAVORITOS="favoritos";
    //nome da(s) colunas da tabela livros
    private static final String ID="id", IDARTIGO="titulo", IDPERFIL="idperfil";

    private final SQLiteDatabase db;


    public FavoritoDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCriarTabela = "CREATE TABLE " + FAVORITOS +"("
                +ID + " Integer PRIMARY KEY, "
                +IDARTIGO + " Integer NOT NULL, "
                +IDPERFIL+ " Integer NOT NULL);";
        sqLiteDatabase.execSQL(sqlCriarTabela);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //apagar tabelas e criar de novo
        String sqlDelTabela = "DROP TABLE IF EXISTS " + FAVORITOS;
        sqLiteDatabase.execSQL(sqlDelTabela);
        onCreate(sqLiteDatabase);
    }

    //metodos CRUD

    /*public Favorito adicionarFavoritoBD(Favorito f){
        ContentValues values = new ContentValues();
        values.put(IDARTIGO,f.getIdartigo());
        values.put(IDPERFIL,f.getIdperfil());


        //devolve -1 se nao conseguir inserir
        long id = this.db.insert(FAVORITOS, null, values);

        if(id>-1){
            f.setId((int)id);
            return f;
        }
        return null;
    }*/

    public boolean removerFavoritoBD(int id){
        int nLinhas = this.db.delete(FAVORITOS, ID+"=?", new String[]{id+""});
        return nLinhas == 1;
    }





    /*public ArrayList<Favorito> getAllFavoritosBD() {
        ArrayList<Favorito> favoritos = new ArrayList<>();

        // A consulta deve garantir que as colunas estão corretas
        Cursor cursor = this.db.query(FAVORITOS, new String[]{ID, IDARTIGO, IDPERFIL}, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                // Ajuste dos índices para corresponder à consulta SQL
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                int idartigo = cursor.getInt(cursor.getColumnIndex(IDARTIGO));
                int idperfil = cursor.getInt(cursor.getColumnIndex(IDPERFIL));

                // Criação do objeto Favorito
                Favorito auxFavorito = new Favorito(id, idartigo, idperfil);
                favoritos.add(auxFavorito);
            } while(cursor.moveToNext());
            cursor.close();
        }
        return favoritos;
    }*/

    public Artigo getArtigoById(int idartigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Artigo artigo = null;

        // Consulta para buscar o artigo com base no idartigo
        Cursor cursor = db.query(
                "artigos",  // Nome da tabela dos artigos
                null,       // Seleciona todas as colunas
                "id = ?",   // Condição para encontrar o artigo pelo id
                new String[]{String.valueOf(idartigo)},  // O id do artigo
                null,       // Não agrupar
                null,       // Não ordenar
                null        // Não limitar
        );

        if (cursor != null && cursor.moveToFirst()) {
            artigo = new Artigo();
            artigo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            artigo.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            artigo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            artigo.setPrecoAnuncio(cursor.getDouble(cursor.getColumnIndex("precoanuncio")));
            artigo.setComissao(cursor.getDouble(cursor.getColumnIndex("comissao")));
            artigo.setEstado(cursor.getString(cursor.getColumnIndex("estado")));
            artigo.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
            artigo.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
            artigo.setTamanho(cursor.getString(cursor.getColumnIndex("tamanho")));
            artigo.setTipoArtigo(cursor.getString(cursor.getColumnIndex("tipoartigo")));
            artigo.setAtivo(cursor.getString(cursor.getColumnIndex("ativo")));
            artigo.setFotos(getFotosByArtigoId(idartigo));  // Supondo que tens um método para buscar as fotos
            cursor.close();
        }

        return artigo; // Retorna o artigo ou null se não encontrar
    }


    private List<String> getFotosByArtigoId(int idartigo) {
        List<String> fotos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obter as fotos do artigo
        Cursor cursor = db.query(
                "fotosartigos",  // Nome da tabela das fotos
                new String[]{"url"}, // Coluna com a URL das fotos
                "idartigo = ?",  // Condição para encontrar as fotos do artigo
                new String[]{String.valueOf(idartigo)},  // O idartigo
                null, null, null
        );

        while (cursor != null && cursor.moveToNext()) {
            fotos.add(cursor.getString(cursor.getColumnIndex("url")));  // Adiciona a URL da foto
        }

        if (cursor != null) {
            cursor.close();
        }

        return fotos;
    }
}
