package com.example.retroverse.Models;

public class Tipopagamento {

    int id;
    String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    // Método toString para exibir o nome no AutoCompleteTextView
    @Override
    public String toString() {
        return descricao;
    }
}
