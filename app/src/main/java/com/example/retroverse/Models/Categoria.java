package com.example.retroverse.Models;

public class Categoria {
    int id;
    String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    @Override
    public String toString() {
        return nome;  // Retorna apenas o nome para exibição no dropdown
    }
}
