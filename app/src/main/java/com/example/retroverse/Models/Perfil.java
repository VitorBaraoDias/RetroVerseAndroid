package com.example.retroverse.Models;

import com.example.retroverse.Singleton.Singleton;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Perfil implements Serializable {
    private int id;
    private String descricao;
    private String username;
    private String caminhofotoperfil;
    private String morada;
    private double saldo;
    private double saldopendente;
    private int banido;
    private int quantidadeAvaliacoes;
    private float mediaAvaliacoes;
    @SerializedName("artigospublicados")
    private ArrayList<Artigo> artigosPublicados;

    @SerializedName("artigosvendidos")
    private ArrayList<Artigo> artigosVendidos;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getUsername() {
        return this.username;
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public double getSaldopendente() {
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

    public int getQuantidadeAvaliacoes() {
        return quantidadeAvaliacoes;
    }

    public void setQuantidadeAvaliacoes(int quantidadeAvaliacoes) {
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
    }

    public float getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }

    public void setMediaAvaliacoes(float mediaAvaliacoes) {
        this.mediaAvaliacoes = mediaAvaliacoes;
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
/*
    public String getFotoperfil() {
        if (caminhofotoperfil != null) {
            return baseUrl + caminhofotoperfil;
        }
        return null;
    }*/

    public String getFotoperfil() {
        if (caminhofotoperfil != null) {
            String dynamicServerIp = Singleton.getInstance(null).getDynamicServerIp();
            return "http://" + dynamicServerIp + "/RetroVerse/frontend/web/uploads/img-profile/" + caminhofotoperfil;
        }
        return null;
    }

}
