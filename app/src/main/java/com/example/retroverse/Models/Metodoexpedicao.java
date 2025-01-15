package com.example.retroverse.Models;

public class Metodoexpedicao {

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
    // Método toString para exibir o nome no AutoCompleteTextView
    @Override
    public String toString() {
        return nome;
    }
}
