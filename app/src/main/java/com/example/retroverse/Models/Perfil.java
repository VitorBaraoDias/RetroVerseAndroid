package com.example.retroverse.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Perfil {
    private int id;
    private String descricao;
    private String username;
    private String caminhofotoperfil;
    private String morada;
    private int saldo;
    private int saldopendente;
    private int banido;

    @SerializedName("artigospublicados")
    private ArrayList<Artigo> artigosPublicados;

    @SerializedName("artigosvendidos")
    private ArrayList<Artigo> artigosVendidos;

    private final String baseUrl = "http://10.0.2.2/defesa-2/RetroVerse/frontend/web/uploads/img-profile/";


    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String setCaminhofotoperfil(String caminhofotoperfil) {
        return this.caminhofotoperfil = caminhofotoperfil;
    }

    public String getCaminhofotoperfil() {
        return caminhofotoperfil;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getSaldopendente() {
        return saldopendente;
    }

    public void setSaldopendente(int saldopendente) {
        this.saldopendente = saldopendente;
    }

    public int getBanido() {
        return banido;
    }

    public void setBanido(int banido) {
        this.banido = banido;
    }

    public ArrayList<Artigo> getArtigosPublicados() {
        return artigosPublicados;
    }

    public void setArtigosPublicados(ArrayList<Artigo> artigosPublicados) {
        this.artigosPublicados = artigosPublicados;
    }

    public ArrayList<Artigo> getArtigosVendidos() {
        return artigosVendidos;
    }

    public void setArtigosVendidos(ArrayList<Artigo> artigosVendidos) {
        this.artigosVendidos = artigosVendidos;
    }

    public String getFotoperfil() {
        if (caminhofotoperfil != null) {
            return baseUrl + caminhofotoperfil;
        }
        return null;
    }
}
