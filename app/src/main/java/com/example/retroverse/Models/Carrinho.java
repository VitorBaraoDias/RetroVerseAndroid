package com.example.retroverse.Models;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class Carrinho {
    private int id;
    private int iduser;

    @SerializedName("linhascarrinho")  // Nome correspondente ao campo no JSON
    private ArrayList<Artigo> linhasCarrinho = new ArrayList<>();



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public ArrayList<Artigo> getLinhasCarrinho() {
        return linhasCarrinho;
    }

    public void setLinhasCarrinho(ArrayList<Artigo> linhasCarrinho) {
        this.linhasCarrinho = linhasCarrinho;
    }
    // Método para calcular o preço total dos artigos no carrinho
    public double getTotalPriceCart() {
        double total = 0.0;

        for (Artigo artigo : linhasCarrinho) {
            total += artigo.getPrecoAnuncio();
        }

        return total;
    }

    // Método opcional para obter o preço total formatado
    public String getTotalPriceFormatted() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
        return format.format(getTotalPriceCart()) + "€";
    }
}
