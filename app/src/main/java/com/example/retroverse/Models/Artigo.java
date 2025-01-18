package com.example.retroverse.Models;

import com.example.retroverse.Singleton.Singleton;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class Artigo implements Serializable {
    private int id;
    @SerializedName("datacriacao")
    private String dataCriacao;
    private String nome;
    private String descricao;
    @SerializedName("precoanuncio")
    private double precoAnuncio;
    private double comissao;
    private String estado;
    private String marca;
    private String categoria;
    private String tamanho;
    @SerializedName("tipoartigo")
    private String tipoArtigo;
    private String ativo;
    private List<String> fotos;
    private boolean premium = false;
    private boolean isLiked = false;
    private Perfil perfil;
    private final String baseUrl = "http://10.0.2.2/RetroVerse/frontend/web/uploads/img-artigos/";

    // Getters e Setters (pode usar o Lombok para gerar isso automaticamente)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoAnuncio() {
        return precoAnuncio;
    }

    public void setPrecoAnuncio(double precoAnuncio) {
        this.precoAnuncio = precoAnuncio;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getTipoArtigo() {
        return tipoArtigo;
    }

    public void setTipoArtigo(String tipoArtigo) {
        this.tipoArtigo = tipoArtigo;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

/*    public String getPrimeiraFotoUrl() {
        if (fotos != null && !fotos.isEmpty()) {
            return baseUrl + fotos.get(0);
        }
        return null;
    }*/

    public String getPrimeiraFotoUrl() {
        if (fotos != null && !fotos.isEmpty()) {
            String dynamicServerIp = Singleton.getInstance(null).getDynamicServerIp();
            return "http://" + dynamicServerIp + "/RetroVerse/frontend/web/uploads/img-artigos/" + fotos.get(0);
        }
        return null;
    }

    public String getPrecoFormatado() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);

        return format.format(precoAnuncio) + "€";
    }
}
