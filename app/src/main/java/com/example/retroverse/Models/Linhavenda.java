package com.example.retroverse.Models;

import java.io.Serializable;

public class Linhavenda implements Serializable {

    int id;
    String nomevendedor;
    String estadoencomenda;

    Artigo artigo;

    public Artigo getArtigo() {
        return artigo;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomevendedor() {
        return nomevendedor;
    }

    public void setNomevendedor(String nomevendedor) {
        this.nomevendedor = nomevendedor;
    }

    public String getEstadoencomenda() {
        return estadoencomenda;
    }

    public void setEstadoencomenda(String estadoencomenda) {
        this.estadoencomenda = estadoencomenda;
    }
}
