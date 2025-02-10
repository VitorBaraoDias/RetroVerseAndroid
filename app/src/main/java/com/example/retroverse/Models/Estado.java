package com.example.retroverse.Models;

public class Estado {
    int id;
    String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return descricao;
    }

    public void setEstado(String estado) {
        this.descricao = estado;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
