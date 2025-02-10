package com.example.retroverse.Models;

public class Tamanho {
    int id;
    String tamanho;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }
    @Override
    public String toString() {
        return tamanho;
    }
}
